package controller;

import domain.Book;
import storage.BookRepository;

public class BookEditDeleteController {
    private BookRepository repository;

    public BookEditDeleteController(BookRepository repository) {
        this.repository = repository;
    }

    public void updateBook(Book book) {
        repository.updateBook(book);
    }

    public void deleteBook(Book book) {
        repository.deleteBook(book);
    }
}
