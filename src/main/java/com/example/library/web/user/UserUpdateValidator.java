package com.example.library.web.user;


import com.example.library.web.ErrorsResource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUpdateValidator {

    ErrorsResource validate(UserResource resource) {
        List<String> validationErrors = new ArrayList<>();

        if(resource.getLastName() == null || resource.getLastName().isEmpty()) {
            validationErrors.add("Last name not specified");
        }

        return new ErrorsResource(validationErrors);
    }
}
