package com.example.library.web.book;

import com.example.library.domain.book.BookService;
import com.example.library.web.ErrorsResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookUpdateValidator {

    private BookService service;

    public BookUpdateValidator(BookService service) {
        this.service = service;
    }

    ErrorsResource validate(BookResource resource) {
        List<String> validationErrors = new ArrayList<>();

        if (resource.getTitle() == null || resource.getTitle().isEmpty()) {
            validationErrors.add("Title not specified");
        }
        if (resource.getAuthor() == null || resource.getAuthor().isEmpty()) {
            validationErrors.add("Author not specified");
        }

        return new ErrorsResource(validationErrors);
    }


}
