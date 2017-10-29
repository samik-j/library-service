package com.example.library.web;

import com.example.library.domain.Edition;
import com.example.library.domain.EditionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books/{bookId}/editions")
public class EditionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditionController.class);
    private EditionService service;

    @Autowired
    public EditionController(EditionService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST)
    public EditionResource addEdition(@PathVariable long bookId, @RequestBody EditionResource resource) {
        LOGGER.info("book id: " + bookId +
                ", added edition: " + resource.getIsbn() + ", quantity " + resource.getQuantity());
        Edition edition = service.registerEdition(bookId, resource);

        return getEditionResource(edition);
    }

    private EditionResource getEditionResource(Edition edition) {
        return new EditionResource(edition.getId(), edition.getIsbn(), edition.getQuantity());
    }

    // z borrow zwroci true albo false
}
