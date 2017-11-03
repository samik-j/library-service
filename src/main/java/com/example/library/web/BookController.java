package com.example.library.web;

import com.example.library.domain.Book;
import com.example.library.domain.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/books")
public class BookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);
    private BookService service;

    @Autowired //wstrzykuje zaleznosc bean ktory jest zadeklarowany
    public BookController(BookService service) {
        this.service = service;
    }

    @RequestMapping
    public List<BookResource> getBooks(@RequestParam(required = false) String title, @RequestParam(required = false) String author) {
        LOGGER.info("Books filtered by: title: {}, author: {}", title, author);

        return getBookResources(service.findBooks(title, author));
    }

    @RequestMapping("/{bookId}") //tego uzywac tylko jezeli zmienna jest identyfikatorem obiektu
    public ResponseEntity getBook(@PathVariable long bookId) {
        Book book = service.findBookById(bookId);

        if(book != null) {
            return new ResponseEntity(getBookResource(book), HttpStatus.OK);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public BookResource addBook(@RequestBody BookResource resource) {
        LOGGER.info("Book added: title: {}, author: {}", resource.getTitle(), resource.getAuthor());
        Book book = service.registerBook(resource);

        return getBookResource(book);
    }

    @RequestMapping(value = "/{bookId}", method = RequestMethod.PUT)
    public BookResource updateBook(@PathVariable long bookId, @RequestBody BookResource resource) {
        Book book = service.updateBook(bookId, resource);

        return getBookResource(book);
    }

    private BookResource getBookResource(Book book) {
        return new BookResource(book);
    }

    private List<BookResource> getBookResources(Collection<Book> books) {
        List<BookResource> bookResources = new ArrayList<>();

        for(Book book : books) {
            bookResources.add(getBookResource(book));
        }

        return bookResources;
    }
}
