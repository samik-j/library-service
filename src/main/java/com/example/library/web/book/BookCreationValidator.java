package com.example.library.web.book;

import com.example.library.domain.book.BookService;
import com.example.library.web.ErrorsResource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookCreationValidator {

    private BookService bookService;

    public BookCreationValidator(BookService bookService) {
        this.bookService = bookService;
    }

    ErrorsResource validate(BookResource resource) {
        List<String> validationErrors = new ArrayList<>();

        if (resource.getTitle() == null || resource.getTitle().isEmpty()) {
            validationErrors.add("Title not specified");
        }
        if (resource.getAuthor() == null || resource.getAuthor().isEmpty()) {
            validationErrors.add("Author not specified");
        }
        if (resource.getTitle() != null &&
                resource.getAuthor() != null &&
                !isBookUnique(resource)) {
            validationErrors.add("Book already exists");
        }

        return new ErrorsResource(validationErrors);
    }

    private boolean isBookUnique(BookResource resource) {
        return !bookService.bookExists(resource);
    }
}
