package com.example.library.web;

import com.example.library.domain.Edition;
import com.example.library.domain.EditionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EditionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EditionController.class);
    private EditionService service;

    @Autowired
    public EditionController(EditionService service) {
        this.service = service;
    }

    @RequestMapping(value = "/books/{bookId}/editions/add", method = RequestMethod.POST)
    public String addEdition(@PathVariable long bookId, @RequestBody EditionResource resource) {
        LOGGER.info("book id: " + bookId +
                ", added edition: " + resource.getIsbn() + ", quantity " + resource.getQuantity());
        service.registerEdition(bookId, resource);
        return "edition added";
    }
}
