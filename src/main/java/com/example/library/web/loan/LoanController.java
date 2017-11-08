package com.example.library.web.loan;

import com.example.library.domain.loan.Loan;
import com.example.library.domain.loan.LoanService;
import com.example.library.web.ErrorsResource;
import com.example.library.web.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoanController.class);
    private LoanService service;
    private LoanCreationValidator creationValidator;
    private LoanReturnValidator returnValidator;

    @Autowired
    public LoanController(LoanService service, LoanCreationValidator creationValidator, LoanReturnValidator returnValidator) {
        this.service = service;
        this.creationValidator = creationValidator;
        this.returnValidator = returnValidator;
    }

    @RequestMapping(method = RequestMethod.GET, params = {"overdue"})
    public List<LoanResource> getLoans(@RequestParam LoanOverdue overdue) {
        LOGGER.info("Lent books overdue: {}", overdue);

        return getLoanResources(service.findLoans(overdue));
    }

    @RequestMapping(method = RequestMethod.GET, params = {"returned"})
    public List<LoanResource> getLoans(@RequestParam boolean returned) {
        LOGGER.info("Lent books: {}", returned ? "returned" : "not returned");

        return getLoanResources(service.findLoans(returned));
    }

    @RequestMapping(method = RequestMethod.GET, params = {"bookId"})
    public List<LoanResource> getLoans(@RequestParam long bookId) {
        return getLoanResources(service.findLoans(bookId));
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<LoanResource> getLoans() {
        LOGGER.info("Lent books");

        return getLoanResources(service.findLoans());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> lend(@RequestBody LoanResource resource) {
        LOGGER.info("Loan for userId: {}, editionId: {}", resource.getUserId(), resource.getEditionId());

        ErrorsResource errorsResource = creationValidator.validate(resource);

        if (errorsResource.getValidationErrors().isEmpty()) {
            Loan loan = service.registerLoan(resource);

            return new ResponseEntity<Object>(getLoanResource(loan), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(errorsResource, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{loanId}", method = RequestMethod.PUT)
    public ResponseEntity<Object> returnLoan(@PathVariable long loanId) {
        LOGGER.info("Loan returned: {}", loanId);

        validateLoanExistence(loanId);

        ErrorsResource errorsResource = returnValidator.validate(getLoanResource(service.findLoan(loanId)));

        if (errorsResource.getValidationErrors().isEmpty()) {
            Loan returned = service.returnLoan(loanId);

            return new ResponseEntity<Object>(getLoanResource(returned), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(errorsResource, HttpStatus.BAD_REQUEST);
        }
    }

    private LoanResource getLoanResource(Loan loan) {
        return new LoanResource(loan);
    }

    private List<LoanResource> getLoanResources(List<Loan> loans) {
        List<LoanResource> loanResources = new ArrayList<>();

        for (Loan loan : loans) {
            loanResources.add(getLoanResource(loan));
        }

        return loanResources;
    }

    private void validateLoanExistence(long loanId) {
        if (!service.loanExists(loanId)) {
            throw new ResourceNotFoundException();
        }
    }
}
