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
    public Set<EditionResource> getEditionsFiltered(@RequestParam(required = false) String editionIsbn) {
        LOGGER.info("Filtered editions");
        LOGGER.info("isbn: " + editionIsbn);

        return getEditionResources(service.findByIsbn(editionIsbn));
    }

    @RequestMapping(method = RequestMethod.POST)
    public EditionResource addEdition(@PathVariable long bookId, @RequestBody EditionResource resource) {
        LOGGER.info("Book id: " + bookId +
                ", added edition: " + resource.getIsbn() + ", quantity " + resource.getQuantity());
        Edition edition = service.registerEdition(bookId, resource);

        return getEditionResource(edition);
    }

    @RequestMapping(value = "/{editionId}", method = RequestMethod.PUT)
    public boolean borrowEdition(@PathVariable long bookId, @PathVariable long editionId) {
        LOGGER.info("Book id: {}, borrowed edition id: {}", bookId, editionId);

        return service.borrow(editionId);
    }

    private EditionResource getEditionResource(Edition edition) {
        return new EditionResource(edition);
    }

    private Set<EditionResource> getEditionResources(List<Edition> editions) {
        Set<EditionResource> editionResources = new HashSet<>();

        for(Edition edition : editions)
            editionResources.add(getEditionResource(edition));

        return editionResources;
    }
    
}
