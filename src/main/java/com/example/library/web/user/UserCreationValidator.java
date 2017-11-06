package com.example.library.web.user;

import com.example.library.domain.user.UserService;
import com.example.library.web.ErrorsResource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserCreationValidator {

    private UserService service;

    public UserCreationValidator (UserService service) {
        this.service = service;
    }

    ErrorsResource validate (UserResource resource) {
        List<String> validationErrors = new ArrayList<> ();

        if (resource.getFirstName () == null || resource.getFirstName ().isEmpty ()) {
            validationErrors.add ("First name not specified");
        }
        if (resource.getLastName () == null || resource.getLastName ().isEmpty ()) {
            validationErrors.add ("Last name not specified");
        }
        if (resource.getFirstName () != null &&
                resource.getLastName () != null &&
                !validateUniqueUser (resource)) {
            validationErrors.add ("User already exists");
        }
        return new ErrorsResource (validationErrors);
    }

    private boolean validateUniqueUser (UserResource resource) {
        return service.hasNoSuchUser (resource);
    }
}
