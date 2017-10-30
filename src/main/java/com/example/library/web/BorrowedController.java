package com.example.library.web;

import com.example.library.domain.BorrowedService;
import com.example.library.domain.Borrowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/borrow")
public class BorrowedController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BorrowedController.class);
    private BorrowedService service;

    @Autowired
    public BorrowedController(BorrowedService service) {
        this.service = service;
    }

    @RequestMapping( method = RequestMethod.POST)
    public boolean borrow(@RequestBody BorrowedResource resource) {
        LOGGER.info("Borrowed by userId: {}, editionId: {}", resource.getUserId(), resource.getEditionId());

        return service.registerBorrowed(resource);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Set<BorrowedResource> getBorrowed() {
        LOGGER.info("Borrowed books");

        return getBorrowedResources(service.findBorrowed());
    }

    private BorrowedResource getBorrowedResource(Borrowed borrowed) {
        return new BorrowedResource(borrowed);
    }

    private Set<BorrowedResource> getBorrowedResources(List<Borrowed> borrowed) {
        Set<BorrowedResource> borrowedResources = new HashSet<>();

        for(Borrowed borrowedOne : borrowed) {
            borrowedResources.add(getBorrowedResource(borrowedOne));
        }

        return borrowedResources;
    }
}
