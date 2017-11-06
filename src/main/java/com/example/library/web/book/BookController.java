package com.example.library.web.book;

import com.example.library.domain.book.Book;
import com.example.library.domain.book.BookService;
import com.example.library.web.ErrorsResource;
import com.example.library.web.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);
    private BookService service;
    private BookCreationValidator validator;

    @Autowired //wstrzykuje zaleznosc bean ktory jest zadeklarowany
    public BookController(BookService service, BookCreationValidator validator) {
        this.service = service;
        this.validator = validator;
    }

    @RequestMapping
    public List<BookResource> getBooks(@RequestParam(required = false) String title, @RequestParam(required = false) String author) {
        LOGGER.info("Books filtered by: title: {}, author: {}", title, author);

        return getBookResources(service.findBooks(title, author));
    }

    @RequestMapping("/{bookId}") //tego uzywac tylko jezeli zmienna jest identyfikatorem obiektu
    public BookResource getBook(@PathVariable long bookId) {
        Book book = service.findBookById(bookId);

        if(book != null) {
            return getBookResource(book);
        }
        else {
            throw new ResourceNotFoundException();
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> addBook(@RequestBody BookResource resource) {
        LOGGER.info("Book added: title: {}, author: {}", resource.getTitle(), resource.getAuthor());

        ErrorsResource errorsResource = validator.validate(resource);

        if(errorsResource.getValidationErrors().isEmpty()) {
            Book book = service.registerBook(resource);

            return new ResponseEntity<Object>(getBookResource(book), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<Object>(errorsResource, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{bookId}", method = RequestMethod.PUT)
    public BookResource updateBook(@PathVariable long bookId, @RequestBody BookResource resource) {

        if(service.bookExists(bookId)) {
            Book book = service.updateBook(bookId, resource);

            return getBookResource(book);
        }
        else {
            throw new ResourceNotFoundException();
        }
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
