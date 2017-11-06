package com.example.library.web.edition;

import com.example.library.domain.edition.EditionService;
import com.example.library.web.ErrorsResource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EditionCreationValidator {

    private EditionService service;

    public EditionCreationValidator(EditionService service) {
        this.service = service;
    }

    ErrorsResource validate(EditionResource resource) {
        List<String> validationErrors = new ArrayList<>();

        if (resource.getIsbn() == null || resource.getIsbn().isEmpty()) {
            validationErrors.add("Isbn not specified");
        } else {
            if (!validateUniqueIsbn(resource.getIsbn())) {
                validationErrors.add("Isbn already exists");
            }
            if (resource.getIsbn().length() != 8 && resource.getIsbn().length() != 13) {
                validationErrors.add("Wrong isbn format");
            }
        }
        if (resource.getPublicationYear() == null) {
            validationErrors.add("Publication year not specified");
        }
        if (resource.getQuantity() < 0) {
            validationErrors.add("Wrong quantity");
        }

        return new ErrorsResource(validationErrors);
    }

    private boolean validateUniqueIsbn(String isbn) {
        return isbn != null && service.hasNoSuchIsbn(isbn);
    }
}
