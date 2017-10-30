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
    public Set<EditionResource> getEditions(@RequestParam(required = false) String editionIsbn) {
        LOGGER.info("Editions filtered: isbn: {}", editionIsbn);

        return getEditionResources(service.findEditions(editionIsbn));
        //to samo co w book metoda
        // if editionIsbn == null service.findAll()
        // else service.findByIsbn(isbn)?
        // bo tu jest wieksza roznica niz w book bo findAll zwraca list a findByIsbn zwraca jedno Edition
    }

    @RequestMapping(method = RequestMethod.POST)
    public EditionResource addEdition(@PathVariable long bookId, @RequestBody EditionResource resource) { //tu mi tworzy EditionResource z body.
        // i tworzy za pomoca set() tak? i co jak nie podam wszystkich parametrow ktore sa w resource? w sensie id? zostaje puste?
        LOGGER.info("Book id: {}, Edition added: isbn: {}, quantity: {}",
                bookId, resource.getIsbn(), resource.getQuantity());
        Edition edition = service.registerEdition(bookId, resource);

        return getEditionResource(edition);
    }

    /*
    TO TEZ TU JEST NIEPOTRZEBNE
    @RequestMapping(value = "/{editionId}", method = RequestMethod.PUT)
    public boolean borrowEdition(@PathVariable long bookId, @PathVariable long editionId) {
        LOGGER.info("Book id: {}, borrowed Edition id: {}", bookId, editionId);

        return service.borrow(editionId);
    }
    */

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
