package com.example.library.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
public class BookController {

    @RequestMapping("/books")
    public Set<Book> getBooks() {
        Set<Book> books = new HashSet<>();
        books.add(new Book(1, "title1", "author"));
        books.add(new Book(2, "title2", "author"));
        books.add(new Book(3, "title3", "author"));

        return books;
    }
}
