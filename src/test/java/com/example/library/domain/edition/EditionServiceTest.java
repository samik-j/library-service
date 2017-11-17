package com.example.library.domain.edition;

import com.example.library.domain.book.Book;
import com.example.library.domain.book.BookRepository;
import com.example.library.web.edition.EditionResource;
import org.junit.Test;

import java.time.Year;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EditionServiceTest {

    private EditionRepository editionRepository = mock(EditionRepository.class);
    private BookRepository bookRepository = mock(BookRepository.class);

    EditionService service = new EditionService(editionRepository, bookRepository);

    private EditionResource getResource() {
        EditionResource resource = new EditionResource();
        resource.setIsbn("1234567890123");
        resource.setPublicationYear(Year.parse("2000"));
        resource.setQuantity(2);

        return resource;
    }

    @Test
    public void shouldRegisterEdition() {
        // given
        EditionResource resource = getResource();
        Book book = mock(Book.class);
        Edition edition = new Edition(resource.getIsbn(), resource.getPublicationYear(), resource.getQuantity(), book);
        Book bookWithEdition = mock(Book.class);
        bookWithEdition.addEdition(edition);

        long bookId = 1;

        when(bookRepository.findOne(bookId)).thenReturn(book);
        when(editionRepository.save(edition)).thenReturn(edition);
        when(bookRepository.save(book)).thenReturn(bookWithEdition);

        // when
        Edition result = service.registerEdition(bookId, resource);

        // then
        assertEquals(resource.getIsbn(), result.getIsbn());
        assertEquals(resource.getPublicationYear(), result.getPublicationYear());
        assertEquals(resource.getQuantity(), result.getQuantity());
        assertEquals(0, result.getOnLoan());
    }

    @Test
    public void shouldFindEditionsAllByBookId() {
        // given
        Book book = mock(Book.class);
        Edition edition1 = new Edition("1234567890123", Year.parse("2000"), 5, book);
        Edition edition2 = new Edition("1234567890333", Year.parse("2002"), 2, book);
        List<Edition> editions = Arrays.asList(edition1, edition2);

        long bookId = 1;
        String isbn = null;

        when(editionRepository.findEditions(bookId, isbn)).thenReturn(editions);

        // when
        List<Edition> result = service.findEditions(bookId, isbn);

        // then
        assertEquals(2, result.size());
        assertEquals(edition1, result.get(0));
        assertEquals(edition2, result.get(1));
    }

    @Test
    public void shouldFindEditionsByIsbnByBookId() {
        // given
        Book book = mock(Book.class);
        Edition edition1 = new Edition("1234567890123", Year.parse("2000"), 5, book);
        List<Edition> editions = Arrays.asList(edition1);

        long bookId = 1;
        String isbn = "1234567890123";

        when(editionRepository.findEditions(bookId, isbn)).thenReturn(editions);

        // when
        List<Edition> result = service.findEditions(bookId, isbn);

        // then
        assertEquals(1, result.size());
        assertEquals(edition1, result.get(0));
    }

    @Test
    public void editionExistsShouldReturnTrue() {
        // given
        long editionId = 1;

        when(editionRepository.exists(editionId)).thenReturn(true);

        // when
        boolean result = service.editionExists(editionId);

        // then
        assertTrue(result);
    }

    @Test
    public void canBeLendShouldReturnTrueIfEditionCanBeLend() {
        // given
        Edition edition = mock(Edition.class);
        long editionId = 1;

        when(editionRepository.findOne(editionId)).thenReturn(edition);
        when(edition.canBeLend()).thenReturn(true);

        // when
        boolean result = service.canBeLend(editionId);

        // then
        assertTrue(result);
    }

    @Test
    public void canBeReturnedShouldReturnTrueIfEditionCanBeReturned() {
        // given
        Edition edition = mock(Edition.class);
        long editionId = 1;

        when(editionRepository.findOne(editionId)).thenReturn(edition);
        when(edition.canBeReturned()).thenReturn(true);

        // when
        boolean result = service.canBeReturned(editionId);

        // then
        assertTrue(result);
    }

    @Test
    public void canBeReturnedShouldReturnFalseIfEditionCanNotBeReturned() {
        // given
        Edition edition = mock(Edition.class);
        long editionId = 1;

        when(editionRepository.findOne(editionId)).thenReturn(edition);
        when(edition.canBeReturned()).thenReturn(false);

        // when
        boolean result = service.canBeReturned(editionId);

        // then
        assertFalse(result);
    }

    @Test
    public void hasNoSuchIsbnShouldReturnTrueIfEditionDoesNotExistByIsbn() {
        // given
        String isbn = "1234567890123";

        when(editionRepository.existsByIsbn(isbn)).thenReturn(false);

        // when
        boolean result = service.hasNoSuchIsbn(isbn);

        // then
        assertTrue(result);
    }

    @Test
    public void hasNoSuchIsbnShouldReturnFalseIfEditionExistsByIsbn() {
        // given
        String isbn = "1234567890123";

        when(editionRepository.existsByIsbn(isbn)).thenReturn(true);

        // when
        boolean result = service.hasNoSuchIsbn(isbn);

        // then
        assertFalse(result);
    }
    
}