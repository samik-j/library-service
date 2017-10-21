package com.example.library.web;

import com.example.library.domain.Book;
import com.example.library.domain.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
public class BookController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);
    private BookService service;

    @Autowired //wstrzykuje zaleznosc bean ktory jest zadeklarowany
    public BookController(BookService service) {
        this.service = service;
    }

    @RequestMapping("/books")
    public Set<Book> getBooksFiltered(@RequestParam(required = false) String title, @RequestParam(required = false) String author) {
        LOGGER.info("filtered books");
        LOGGER.info("title: " + title + ", author: " + author);
        Set<Book> booksFiltered = new HashSet<>();
        for (Book book : service.findAll()) {
            if ((title == null || book.getTitle().equals(title)) && (author == null || book.getAuthor().equals(author))) {
                booksFiltered.add(book);
            }
        }
        return booksFiltered;
    }

    @RequestMapping("/books/{bookId}") //tego uzywac tylko jezeli zmienna jest identyfikatorem obiektu
    public Book getBook(@PathVariable int bookId) {
        for (Book book : service.findAll()) {
            if (book.getId() == bookId)
                return book;
        }
        return null;
    }

    @RequestMapping(value = "/books/add", method = RequestMethod.POST)
    public String addBook(@RequestBody CreateBookResource resource) {
        LOGGER.info("book added title: " + resource.getTitle() + ", author: " + resource.getAuthor());
        service.registerBook(resource.getTitle(), resource.getAuthor());
        return "book added";
    }


}
