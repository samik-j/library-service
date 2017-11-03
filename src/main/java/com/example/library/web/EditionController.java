package com.example.library.web;

import com.example.library.domain.Edition;
import com.example.library.domain.EditionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/books/{bookId}/editions")
public class EditionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditionController.class);
    private EditionService service;

    @Autowired
    public EditionController(EditionService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<EditionResource> getEditions(@PathVariable long bookId, @RequestParam(required = false) String isbn) {
        LOGGER.info("Editions filtered: isbn: {}", isbn);

        return getEditionResources(service.findEditions(bookId, isbn));
    }

    @RequestMapping(method = RequestMethod.POST)
    public EditionResource addEdition(@PathVariable long bookId, @RequestBody EditionResource resource) {
        LOGGER.info("Book id: {}, Edition added: isbn: {}, quantity: {}",
                bookId, resource.getIsbn(), resource.getQuantity());
        Edition edition = service.registerEdition(bookId, resource);

        return getEditionResource(edition);
    }

    private EditionResource getEditionResource(Edition edition) {
        return new EditionResource(edition);
    }

    private List<EditionResource> getEditionResources(Collection<Edition> editions) {
        List<EditionResource> editionResources = new ArrayList<>();

        for(Edition edition : editions) {
            editionResources.add(getEditionResource(edition));
        }

        return editionResources;
    }

}
