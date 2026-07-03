package controller;

import domain.Book;
import storage.BookRepository;

public class BookRegisterController {
    private BookRepository repository;

    public BookRegisterController(BookRepository repository) {
        this.repository = repository;
    }

    public void registerBook(Book book) {
        repository.saveBook(book);
    }
}
