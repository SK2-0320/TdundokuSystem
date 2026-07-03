package storage;

import java.util.ArrayList;

import domain.Book;
import domain.Bookshelf;

public class BookRepository {
    private Bookshelf bookshelf;

    public BookRepository() {
        bookshelf = new Bookshelf();
    }

    public void saveBook(Book book) {
        bookshelf.addBook(book);
    }

    public void deleteBook(Book book) {
        bookshelf.removeBook(book);
    }

    public ArrayList<Book> findAllBooks() {
        return bookshelf.getAllBooks();
    }

    public ArrayList<Book> findBooks(String keyword) {
        return bookshelf.searchBooks(keyword);
    }
}
