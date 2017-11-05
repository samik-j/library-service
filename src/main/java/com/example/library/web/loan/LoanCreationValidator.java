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

    //to ma byc autowired?
    public LoanCreationValidator(UserService userService, EditionService editionService) {
        this.userService = userService;
        this.editionService = editionService;
    }

    ErrorsResource validate(LoanResource resource) {
        List<String> validationErrors = new ArrayList<>();

        if(!validateUserExistence(resource.getUserId())) {
            validationErrors.add("User does not exist");
        }
        if(!validateEditionExistence(resource.getEditionId())) {
            validationErrors.add("Edition does not exist");
        }
        if(!validateLoan(resource.getEditionId())) {
            validationErrors.add("Not possible to loan because of insufficient quantity available");
        }
        
        return new ErrorsResource(validationErrors);
    }

    private boolean validateUserExistence(long userId) {
        return userService.userExists(userId);
    }

    private boolean validateEditionExistence(long editionId) {
        return editionService.editionExists(editionId);
    }

    private boolean validateLoan(long editionId) {
        return editionService.canBeLend(editionId);
    }
}
