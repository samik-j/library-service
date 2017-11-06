package com.example.library.web.edition;

import com.example.library.domain.book.Book;
import com.example.library.domain.book.BookService;
import com.example.library.domain.edition.Edition;
import com.example.library.domain.edition.EditionService;
import com.example.library.web.ErrorsResource;
import com.example.library.web.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/books/{bookId}/editions")
public class EditionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditionController.class);
    private EditionService service;
    private BookService bookService;
    private EditionCreationValidator validator;

    @Autowired
    public EditionController(EditionService service, BookService bookService, EditionCreationValidator validator) {
        this.service = service;
        this.bookService = bookService;
        this.validator = validator;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<EditionResource> getEditions(@PathVariable long bookId, @RequestParam(required = false) String isbn) {
        LOGGER.info("Editions filtered: isbn: {}", isbn);

        validateBookExistence(bookId);

        return getEditionResources(service.findEditions(bookId, isbn));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> addEdition(@PathVariable long bookId, @RequestBody EditionResource resource) {
        LOGGER.info("Book id: {}, Edition added: isbn: {}, quantity: {}",
                bookId, resource.getIsbn(), resource.getQuantity());

        validateBookExistence(bookId);
        ErrorsResource errorsResource = validator.validate(resource);

        if (errorsResource.getValidationErrors().isEmpty()) {
            Edition edition = service.registerEdition(bookId, resource);

            return new ResponseEntity<Object>(getEditionResource(edition), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(errorsResource, HttpStatus.BAD_REQUEST);
        }
    }

    private EditionResource getEditionResource(Edition edition) {
        return new EditionResource(edition);
    }

    private List<EditionResource> getEditionResources(Collection<Edition> editions) {
        List<EditionResource> editionResources = new ArrayList<>();

        for (Edition edition : editions) {
            editionResources.add(getEditionResource(edition));
        }

        return editionResources;
    }

    private void validateBookExistence(@PathVariable long bookId) {
        Book book = bookService.findBookById(bookId);

        if (book == null) {
            throw new ResourceNotFoundException();
        }
    }

}
