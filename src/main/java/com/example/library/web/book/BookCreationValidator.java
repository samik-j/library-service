package com.example.library.web.book;

import com.example.library.web.ErrorsResource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookCreationValidator {

    ErrorsResource validate(BookResource resource) {
        List<String> validationErrors = new ArrayList<>();

        if(resource.getTitle() == null || resource.getTitle().isEmpty()) {
            validationErrors.add("Title not specified");
        }
        if(resource.getAuthor() == null || resource.getAuthor().isEmpty()) {
            validationErrors.add("Author not specified");
        }

        return new ErrorsResource(validationErrors);
    }
}
