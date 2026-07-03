package controller;

import java.util.ArrayList;

import domain.Book;
import storage.BookRepository;

public class BookSearchController {
    private BookRepository repository;

    public BookSearchController(BookRepository repository) {
        this.repository = repository;
    }

    public ArrayList<Book> searchBooks(String keyword, String filter) {
        // TODO: filter に応じた検索条件の切り替えを実装する
        return repository.findBooks(keyword);
    }
}
