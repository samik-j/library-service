package com.example.library.domain;

import com.example.library.web.EditionResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Edition edition = new Edition(resource.getIsbn(), resource.getQuantity(), book);
        book.addEdition(edition);
        Edition savedEdition = repository.save(edition);
        bookRepository.save(book);
        return savedEdition;
    }

}
