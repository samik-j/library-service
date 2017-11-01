package com.example.library.web;

import com.example.library.domain.Loan;
import com.example.library.domain.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/borrowed")
public class LoanController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoanController.class);
    private LoanService service;

    @Autowired
    public LoanController(LoanService service) {
        this.service = service;
    }

    @RequestMapping( method = RequestMethod.POST)
    public boolean lend(@RequestBody LoanResource resource) {
        LOGGER.info("Loan by userId: {}, editionId: {}", resource.getUserId(), resource.getEditionId());

        return service.registerLoan(resource);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Set<LoanResource> getLoans() {
        LOGGER.info("Lent books");

        return getBorrowedResources(service.findLoans());
    }

    private LoanResource getBorrowedResource(Loan loan) {
        return new LoanResource(loan);
    }

    private Set<LoanResource> getBorrowedResources(List<Loan> loan) {
        Set<LoanResource> loanResources = new HashSet<>();

        for(Loan loanOne : loan) {
            loanResources.add(getBorrowedResource(loanOne));
        }

        return loanResources;
    }
}
