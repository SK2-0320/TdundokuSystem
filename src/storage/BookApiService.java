package storage;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import domain.Book;

public class BookApiService {
    private static final String API_URL = "https://api.openbd.jp/v1/get?isbn=";
    private static final int ISBN_10_LENGTH = 10;
    private static final int ISBN_13_LENGTH = 13;
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private HttpClient httpClient;

    public BookApiService() {
        httpClient = HttpClient.newBuilder()
                .connectTimeout(TIMEOUT)
                .build();
    }

    public Book searchByIsbn(String isbnCode) {
        String normalizedIsbn = normalizeIsbn(isbnCode);
        if (normalizedIsbn == null) {
            return null;
        }

        try {
            String encodedIsbn = URLEncoder.encode(normalizedIsbn, StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + encodedIsbn))
                    .timeout(TIMEOUT)
                    .GET()
                    .build();

            // openBD APIへGETリクエストを送信する。
            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() != 200) {
                return null;
            }

            return createBookFromResponse(response.body(), normalizedIsbn);
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            System.err.println("openBD APIの通信に失敗しました: " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return null;
        }
    }

    // ISBNに含まれるハイフン、半角スペース、全角スペースを除去して検証する。
    private String normalizeIsbn(String isbnCode) {
        if (isbnCode == null) {
            return null;
        }

        String normalizedIsbn = isbnCode
                .replace("-", "")
                .replace(" ", "")
                .replace("　", "")
                .trim();

        if (!normalizedIsbn.matches("\\d+")) {
            return null;
        }

        if (normalizedIsbn.length() != ISBN_10_LENGTH
                && normalizedIsbn.length() != ISBN_13_LENGTH) {
            return null;
        }

        return normalizedIsbn;
    }

    private Book createBookFromResponse(String responseBody, String fallbackIsbn) {
        if (responseBody == null || responseBody.trim().isEmpty()) {
            return null;
        }

        String trimmedResponse = responseBody.trim();
        if ("[null]".equals(trimmedResponse)) {
            return null;
        }

        try {
            String summaryJson = extractObjectByKey(trimmedResponse, "summary");
            if (summaryJson == null) {
                return null;
            }

            String isbn = extractStringValue(summaryJson, "isbn");
            String title = extractStringValue(summaryJson, "title");
            String author = extractStringValue(summaryJson, "author");

            if (title == null && author == null) {
                return null;
            }

            Book book = new Book();
            book.setIsbnCode(isbn != null ? isbn : fallbackIsbn);
            book.setBookName(title);
            book.setWriterName(author);

            return book;
        } catch (IllegalStateException e) {
            System.err.println("openBD APIレスポンスの解析に失敗しました: " + e.getMessage());
            return null;
        }
    }

    // openBDレスポンス内の指定キーに対応するJSONオブジェクト部分を取り出す。
    private String extractObjectByKey(String json, String key) {
        int keyIndex = findKeyIndex(json, key, 0);
        if (keyIndex < 0) {
            return null;
        }

        int colonIndex = findNextNonStringCharacter(json, ':', keyIndex);
        if (colonIndex < 0) {
            return null;
        }

        int objectStart = findNextNonWhitespaceIndex(json, colonIndex + 1);
        if (objectStart < 0 || json.charAt(objectStart) != '{') {
            return null;
        }

        int objectEnd = findMatchingBrace(json, objectStart);
        if (objectEnd < 0) {
            throw new IllegalStateException("summaryオブジェクトの終端が見つかりません");
        }

        return json.substring(objectStart, objectEnd + 1);
    }

    // 指定されたキーの文字列値を取り出す。
    private String extractStringValue(String json, String key) {
        int keyIndex = findKeyIndex(json, key, 0);
        if (keyIndex < 0) {
            return null;
        }

        int colonIndex = findNextNonStringCharacter(json, ':', keyIndex);
        if (colonIndex < 0) {
            return null;
        }

        int valueStart = findNextNonWhitespaceIndex(json, colonIndex + 1);
        if (valueStart < 0 || json.charAt(valueStart) != '"') {
            return null;
        }

        return readJsonString(json, valueStart);
    }

    private int findKeyIndex(String json, String key, int startIndex) {
        String target = "\"" + key + "\"";
        int index = startIndex;

        while (index < json.length()) {
            int foundIndex = json.indexOf(target, index);
            if (foundIndex < 0) {
                return -1;
            }

            if (!isInsideString(json, foundIndex)) {
                return foundIndex;
            }

            index = foundIndex + target.length();
        }

        return -1;
    }

    private int findNextNonStringCharacter(String json, char target, int startIndex) {
        boolean inString = false;
        boolean escaped = false;

        for (int i = startIndex; i < json.length(); i++) {
            char current = json.charAt(i);

            if (escaped) {
                escaped = false;
                continue;
            }

            if (current == '\\') {
                escaped = true;
                continue;
            }

            if (current == '"') {
                inString = !inString;
                continue;
            }

            if (!inString && current == target) {
                return i;
            }
        }

        return -1;
    }

    private int findNextNonWhitespaceIndex(String json, int startIndex) {
        for (int i = startIndex; i < json.length(); i++) {
            if (!Character.isWhitespace(json.charAt(i))) {
                return i;
            }
        }

        return -1;
    }

    private int findMatchingBrace(String json, int objectStart) {
        boolean inString = false;
        boolean escaped = false;
        int depth = 0;

        for (int i = objectStart; i < json.length(); i++) {
            char current = json.charAt(i);

            if (escaped) {
                escaped = false;
                continue;
            }

            if (current == '\\') {
                escaped = true;
                continue;
            }

            if (current == '"') {
                inString = !inString;
                continue;
            }

            if (inString) {
                continue;
            }

            if (current == '{') {
                depth++;
            } else if (current == '}') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }

        return -1;
    }

    private String readJsonString(String json, int quoteIndex) {
        StringBuilder value = new StringBuilder();
        boolean escaped = false;

        for (int i = quoteIndex + 1; i < json.length(); i++) {
            char current = json.charAt(i);

            if (escaped) {
                appendEscapedCharacter(value, current);
                escaped = false;
                continue;
            }

            if (current == '\\') {
                escaped = true;
                continue;
            }

            if (current == '"') {
                return value.toString();
            }

            value.append(current);
        }

        throw new IllegalStateException("文字列の終端が見つかりません");
    }

    private void appendEscapedCharacter(StringBuilder value, char current) {
        if (current == '"') {
            value.append('"');
        } else if (current == '\\') {
            value.append('\\');
        } else if (current == '/') {
            value.append('/');
        } else if (current == 'b') {
            value.append('\b');
        } else if (current == 'f') {
            value.append('\f');
        } else if (current == 'n') {
            value.append('\n');
        } else if (current == 'r') {
            value.append('\r');
        } else if (current == 't') {
            value.append('\t');
        } else {
            value.append(current);
        }
    }

    private boolean isInsideString(String json, int index) {
        boolean inString = false;
        boolean escaped = false;

        for (int i = 0; i < index; i++) {
            char current = json.charAt(i);

            if (escaped) {
                escaped = false;
                continue;
            }

            if (current == '\\') {
                escaped = true;
                continue;
            }

            if (current == '"') {
                inString = !inString;
            }
        }

        return inString;
    }
}
