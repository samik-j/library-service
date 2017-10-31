package com.example.library.web;

import com.example.library.domain.Edition;
import com.example.library.domain.EditionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public Set<EditionResource> getEditions(@RequestParam(required = false) String editionIsbn) {//dodac bookId
        LOGGER.info("Editions filtered: isbn: {}", editionIsbn);

        return getEditionResources(service.findEditions(editionIsbn));
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

    private Set<EditionResource> getEditionResources(List<Edition> editions) {
        Set<EditionResource> editionResources = new HashSet<>();

        for(Edition edition : editions) {
            editionResources.add(getEditionResource(edition));
        }

        return editionResources;
    }

}
