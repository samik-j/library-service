package com.example.library.web.loan;

import com.example.library.domain.edition.EditionService;
import com.example.library.domain.user.UserService;
import com.example.library.web.ErrorsResource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LoanCreationValidator {

    private UserService userService;
    private EditionService editionService;

    //jest autowired automatycznie
    public LoanCreationValidator(UserService userService, EditionService editionService) {
        this.userService = userService;
        this.editionService = editionService;
    }

    ErrorsResource validate(LoanResource resource) {
        List<String> validationErrors = new ArrayList<>();

        if (resource.getUserId() == null) {
            validationErrors.add("User id not specified");
        } else {
            if (!userExists(resource.getUserId())) {
                validationErrors.add("User does not exist");
            } else {
                if (!userCanBorrow(resource.getUserId())) {
                    validationErrors.add("Not possible to loan because user has reached borrowing limit");
                }
            }
        }
        if (resource.getEditionId() == null) {
            validationErrors.add("Edition id not specified");
        } else {
            if (!editionExists(resource.getEditionId())) {
                validationErrors.add("Edition does not exist");
            } else {
                if (!editionCanBeLend(resource.getEditionId())) {
                    validationErrors.add("Not possible to loan because of insufficient quantity available");
                }
            }
        }

        return new ErrorsResource(validationErrors);
    }

    private boolean userExists(long userId) {
        return userService.userExists(userId);
    }

    private boolean editionExists(long editionId) {
        return editionService.editionExists(editionId);
    }

    private boolean editionCanBeLend(long editionId) {
        return editionService.canBeLend(editionId);
    }

    private boolean userCanBorrow(long userId) {
        return userService.canBorrow(userId);
    }
}
