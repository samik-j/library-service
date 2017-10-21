package com.example.library.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private BookRepository repository;

    @Autowired
    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public void registerBook(String title, String author) {
        Book book = new Book(title, author);
        repository.save(book);
    }

    public List<Book> findAll() {
        return repository.findAll();
    }
}
