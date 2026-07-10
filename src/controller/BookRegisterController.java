package controller;

import domain.Book;
import storage.BookApiService;
import storage.BookRepository;

public class BookRegisterController {
    private BookRepository repository;
    private BookApiService bookApiService;

    public BookRegisterController(
            BookRepository repository,
            BookApiService bookApiService) {
        this.repository = repository;
        this.bookApiService = bookApiService;
    }

    public void registerBook(Book book) {
        repository.saveBook(book);
    }

    public Book searchBookByIsbn(String isbnCode) {
        return bookApiService.searchByIsbn(isbnCode);
    }
}
