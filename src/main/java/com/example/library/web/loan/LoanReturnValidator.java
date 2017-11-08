package com.example.library.web.loan;

import com.example.library.domain.edition.EditionService;
import com.example.library.domain.loan.LoanService;
import com.example.library.domain.user.UserService;
import com.example.library.web.ErrorsResource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LoanReturnValidator {

    private UserService userService;
    private EditionService editionService;
    private LoanService loanService;

    public LoanReturnValidator(UserService userService, EditionService editionService, LoanService loanService) {
        this.userService = userService;
        this.editionService = editionService;
        this.loanService = loanService;
    }

    ErrorsResource validate(LoanResource resource) {
        List<String> validationErrors = new ArrayList<>();

        if (!validateLoanCanBeReturned(resource.getId())) {
            validationErrors.add("Loan has been returned");
        } else {
            if (!validateBorrowedByUser(resource.getUserId())) {
                validationErrors.add("User has no borrowed books");
            }
            if (!validateOnLoanEdition(resource.getEditionId())) {
                validationErrors.add("Edition has not been loaned");
            }
        }

        return new ErrorsResource(validationErrors);
    }

    private boolean validateLoanCanBeReturned(long loanId) {
        return loanService.canBeReturned(loanId);
    }

    private boolean validateBorrowedByUser(long userId) {
        return userService.canReturn(userId);
    }

    private boolean validateOnLoanEdition(long editionId) {
        return editionService.canBeReturned(editionId);
    }
}
