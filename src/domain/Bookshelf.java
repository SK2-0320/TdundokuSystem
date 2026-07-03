package domain;

import java.util.ArrayList;

public class Bookshelf {
    private ArrayList<Book> books;

    public Bookshelf() {
        books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public ArrayList<Book> getAllBooks() {
        return new ArrayList<Book>(books);
    }

    public ArrayList<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return getAllBooks();
        }

        ArrayList<Book> results = new ArrayList<>();

        // タイトルまたは著者名で検索
        for (Book book : books) {
            if (book == null) {
                continue;
            }

            String bookName = book.getBookName();
            String writerName = book.getWriterName();
            boolean matchesBookName = bookName != null && bookName.contains(keyword);
            boolean matchesWriterName = writerName != null && writerName.contains(keyword);

            if (matchesBookName || matchesWriterName) {
                results.add(book);
            }
        }

        return results;
    }
}
