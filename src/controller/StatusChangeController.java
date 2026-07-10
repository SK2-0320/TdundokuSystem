package controller;

import domain.Book;
import storage.BookRepository;

public class StatusChangeController {
    private BookRepository repository;

    public StatusChangeController(BookRepository repository) {
        this.repository = repository;
    }

    public void changeStatus(Book book, String status) {
        book.setStatus(status);
        repository.updateBook(book);
    }

    public void changeStatusWithDetail(Book book, String status, int rating, String review) {
        book.setStatus(status);
        book.setRating(rating);
        book.setReview(review);
        repository.updateBook(book);
    }
}
