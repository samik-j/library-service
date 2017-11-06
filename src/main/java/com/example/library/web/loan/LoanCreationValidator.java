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
    public LoanCreationValidator (UserService userService, EditionService editionService) {
        this.userService = userService;
        this.editionService = editionService;
    }

    ErrorsResource validate (LoanResource resource) {
        List<String> validationErrors = new ArrayList<> ();

        if (resource.getUserId () == null) {
            validationErrors.add ("User id not specified");
        } else {
            if (!validateUserExistence (resource.getUserId ())) {
                validationErrors.add ("User does not exist");
            } else {
                if (!validateUserBorrowingAvailability (resource.getUserId ())) {
                    validationErrors.add ("Not possible to loan because user has reached borrowing limit");
                }
            }
        }
        if (resource.getEditionId () == null) {
            validationErrors.add ("Edition id not specified");
        } else {
            if (!validateEditionExistence (resource.getEditionId ())) {
                validationErrors.add ("Edition does not exist");
            } else {
                if (!validateEditionAvailability (resource.getEditionId ())) {
                    validationErrors.add ("Not possible to loan because of insufficient quantity available");
                }
            }
        }

        return new ErrorsResource (validationErrors);
    }

    private boolean validateUserExistence (long userId) {
        return userService.userExists (userId);
    }

    private boolean validateEditionExistence (long editionId) {
        return editionService.editionExists (editionId);
    }

    private boolean validateEditionAvailability (long editionId) {
        return editionService.canBeLend (editionId);
    }

    private boolean validateUserBorrowingAvailability (long userId) {
        return userService.canBorrow (userId);
    }
}
