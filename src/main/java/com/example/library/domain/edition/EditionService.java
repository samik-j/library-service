package com.example.library.domain.edition;

import com.example.library.domain.book.Book;
import com.example.library.domain.book.BookRepository;
import com.example.library.web.edition.EditionResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional //albo ma byc transactional albo usunac zmiany na book
    public Edition registerEdition(long bookId, EditionResource resource) {
        Book book = bookRepository.findOne(bookId);
        Edition edition = new Edition(resource.getIsbn(), resource.getPublicationYear(), resource.getQuantity(), book);

        book.addEdition(edition);
        Edition savedEdition = editionRepository.save(edition);
        bookRepository.save(book);

        return savedEdition;
    }

    public List<Edition> findEditions(long bookId, String isbn) {
        return editionRepository.findEditions(bookId, isbn);
    }

    public boolean editionExists(long editionId) {
        return editionRepository.exists(editionId);
    }

    public boolean canBeLend(long editionId) {
        return editionRepository.findOne(editionId).canBeLend();
    }

    public boolean isbnExists(String isbn) {
        return editionRepository.existsByIsbn(isbn);
    }

    public boolean canBeReturned(long editionId) {
        return editionRepository.findOne(editionId).canBeReturned();
    }
}
