package com.example.library.domain;

import com.example.library.web.EditionResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EditionService {

    private EditionRepository editionRepository;
    private BookRepository bookRepository;

    @Autowired
    public EditionService(EditionRepository editionRepository, BookRepository bookRepository) {
        this.editionRepository = editionRepository;
        this.bookRepository = bookRepository;
    }

    //albo ma byc transactional albo usunac zmiany na book
    public Edition registerEdition(long bookId, EditionResource resource) {
        Book book = bookRepository.findOne(bookId);
        Edition edition = new Edition(resource.getIsbn(), resource.getQuantity(), book);
        book.addEdition(edition);
        Edition savedEdition = editionRepository.save(edition);
        bookRepository.save(book);
        return savedEdition;
    }

    public List<Edition> findEditions(String isbn) {
        if(isbn != null) {
            return editionRepository.findByIsbn(isbn);
        }
        else {
            return editionRepository.findAll();
        }
    }

}
