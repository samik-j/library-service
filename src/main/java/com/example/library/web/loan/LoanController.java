package com.example.library.web.loan;

import com.example.library.domain.loan.Loan;
import com.example.library.domain.loan.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoanController.class);
    private LoanService service;

    @Autowired
    public LoanController(LoanService service) {
        this.service = service;
    }

    @RequestMapping( method = RequestMethod.POST)
    public boolean lend(@RequestBody LoanResource resource) {
        LOGGER.info("Loan for userId: {}, editionId: {}", resource.getUserId(), resource.getEditionId());

        return service.registerLoan(resource);
    }

    @RequestMapping(method = RequestMethod.GET, params = {"overdue"})
    public List<LoanResource> getLoans(@RequestParam LoanOverdue overdue) {
        LOGGER.info("Lent books overdue: {}", overdue);

        return getLoanResources(service.findLoans(overdue));
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<LoanResource> getLoans() {
        LOGGER.info("Lent books");

        return getLoanResources(service.findLoans());
    }

    private LoanResource getLoanResource(Loan loan) {
        return new LoanResource(loan);
    }

    private List<LoanResource> getLoanResources(List<Loan> loans) {
        List<LoanResource> loanResources = new ArrayList<>();

        for(Loan loan : loans) {
            loanResources.add(getLoanResource(loan));
        }

        return loanResources;
    }
}
