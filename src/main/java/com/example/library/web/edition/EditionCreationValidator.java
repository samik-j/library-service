package com.example.library.web.edition;

import com.example.library.web.ErrorsResource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EditionCreationValidator {

    ErrorsResource validate(EditionResource resource) {
        List<String> validationErrors = new ArrayList<>();

        if(resource.getIsbn() == null || resource.getIsbn().isEmpty()) {
            validationErrors.add("Isbn not specified");
        }
        else if(resource.getPublicationYear() == null) {
            validationErrors.add("Publication year not specified");
        }
        else if(resource.getQuantity() == null) {
            validationErrors.add("Quantity not specified");
        }
        else if(resource.getQuantity() <0) {
            validationErrors.add("Wrong quantity");
        }

        return new ErrorsResource(validationErrors);
    }
}
