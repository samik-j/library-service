package com.example.library.domain;

import com.example.library.web.EditionResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EditionService {

    private EditionRepository repository;
    private BookRepository bookRepository;

    @Autowired
    public EditionService(EditionRepository repository, BookRepository bookRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
    }

    public Edition registerEdition(long bookId, EditionResource resource) {
        Book book = bookRepository.findOne(bookId);
        Edition edition = new Edition(resource.getIsbn(), resource.getQuantity(), resource.getBorrowed(), book);
        book.addEdition(edition);
        Edition savedEdition = repository.save(edition);
        bookRepository.save(book);
        return savedEdition;
    }

    public List<Edition> findByIsbn(String isbn) {
        if(isbn != null) {
            return repository.findByIsbn(isbn);
        }
        else {
            return repository.findAll();
        }
    }

    public boolean borrow(long editionId) {
        Edition edition = repository.findOne(editionId);
        boolean borrowed = edition.borrow();
        repository.save(edition);
        return borrowed;
    }

}
