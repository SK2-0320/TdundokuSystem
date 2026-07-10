package storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import domain.Book;
import domain.Bookshelf;

public class BookRepository {
    private String filePath;
    private Bookshelf bookshelf;

    public BookRepository() {
        filePath = "books.dat";
        loadFromFile();
    }

    public void saveBook(Book book) {
        bookshelf.addBook(book);
        saveToFile();
    }

    public void deleteBook(Book book) {
        bookshelf.removeBook(book);
        saveToFile();
    }

    public void updateBook(Book book) {
        saveToFile();
    }

    public ArrayList<Book> findAllBooks() {
        return bookshelf.getAllBooks();
    }

    public ArrayList<Book> findBooks(String keyword) {
        return bookshelf.searchBooks(keyword);
    }

    private void saveToFile() {
        try (ObjectOutputStream outputStream =
                new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(bookshelf);
        } catch (IOException e) {
            System.err.println("書籍データの保存に失敗しました: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(filePath);

        if (!file.exists()) {
            bookshelf = new Bookshelf();
            return;
        }

        try (ObjectInputStream inputStream =
                new ObjectInputStream(new FileInputStream(file))) {
            Object object = inputStream.readObject();

            if (object instanceof Bookshelf) {
                bookshelf = (Bookshelf) object;
            } else {
                bookshelf = new Bookshelf();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("書籍データの読み込みに失敗しました: " + e.getMessage());
            bookshelf = new Bookshelf();
        }
    }
}
