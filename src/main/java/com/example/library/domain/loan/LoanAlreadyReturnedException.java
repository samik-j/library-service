package com.example.library.domain.loan;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class LoanAlreadyReturnedException extends RuntimeException {

    LoanAlreadyReturnedException(long id) {
        super("Loan already returned with id " + id);
    }
}
