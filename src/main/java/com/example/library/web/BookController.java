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
@RequestMapping("/books")
public class BookController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);
    private BookService service;

    @Autowired //wstrzykuje zaleznosc bean ktory jest zadeklarowany
    public BookController(BookService service) {
        this.service = service;
    }

    @RequestMapping
    public Set<BookResource> getBooksFiltered(@RequestParam(required = false) String title, @RequestParam(required = false) String author) {
        LOGGER.info("filtered books");
        LOGGER.info("title: " + title + ", author: " + author);

        return this.getBooksPartialMatch(title, author);
    }

    private Set<BookResource> getBooksPartialMatch(String title, String author) {
        Set<BookResource> booksFiltered = new HashSet<>();
        for (Book book : this.service.findAll()) {
            if ((title == null || this.containsIgnoreCase(book.getTitle(), title))
                    && (author == null || this.containsIgnoreCase(book.getAuthor(), author))) {
                booksFiltered.add(getBookResource(book));
            }
        }
        return booksFiltered;
    }

    private boolean containsIgnoreCase(String first, String second) {
        return first.toLowerCase().contains(second.toLowerCase());
    }

    @RequestMapping("/{bookId}") //tego uzywac tylko jezeli zmienna jest identyfikatorem obiektu
    public BookResource getBook(@PathVariable long bookId) {
        Book book = service.findBookById(bookId);

        return getBookResource(book);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BookResource addBook(@RequestBody BookResource resource) {
        LOGGER.info("book added: title: " + resource.getTitle() + ", author: " + resource.getAuthor());
        Book book = this.service.registerBook(resource);

        return getBookResource(book);
    }

    @RequestMapping(value = "/{bookId}/update", method = RequestMethod.POST)
    public BookResource updateBook(@PathVariable long bookId, @RequestBody BookResource resource) {
        Book book = this.service.updateBook(bookId, resource);

        return getBookResource(book);
    }

    private BookResource getBookResource(Book book) {
        return new BookResource(book.getId(), book.getTitle(), book.getAuthor());
    }

}
