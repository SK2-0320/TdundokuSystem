package controller;

import domain.Book;

public class StatusChangeController {
    public void changeStatus(Book book, String status) {
        book.setStatus(status);
    }

    public void changeStatusWithDetail(Book book, String status, int rating, String review) {
        book.setStatus(status);
        book.setRating(rating);
        book.setReview(review);
    }
}
