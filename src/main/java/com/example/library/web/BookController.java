package com.example.library.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
public class BookController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);
    private static final Set<Book> books = new HashSet<>();

    public BookController() {
        books.add(new Book(1, "title1", "author"));
        books.add(new Book(2, "title2", "author"));
        books.add(new Book(3, "title3", "author"));
    }

    @RequestMapping("/books")
    public Set<Book> getBooksFiltered(@RequestParam(required = false) String title, @RequestParam(required = false) String author) {
        LOGGER.info("filtered books");
        LOGGER.info("title: " + title + ", author: " + author);
        Set<Book> booksFiltered = new HashSet<>();
        for(Book book : books) {
            if((title == null || book.getTitle().equals(title)) && (author == null || book.getAuthor().equals(author))) {
                booksFiltered.add(book);
            }
        }
        return booksFiltered;
    }

    @RequestMapping("/books/{bookId}") //tego uzywac tylko jezeli zmienna jest identyfikatorem obiektu
    public Book getBook(@PathVariable int bookId) {
        for(Book book : books) {
            if(book.getId() == bookId)
                return book;
        }
        return null;
    }

    @RequestMapping("/books/add")
    public String addBook(String title, String author) {
        LOGGER.info("book added title: " + title + ", author: " + author);
        return "book added";
    }
}
