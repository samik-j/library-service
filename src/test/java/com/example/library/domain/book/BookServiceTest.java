package com.example.library.domain.book;

import com.example.library.web.book.BookResource;
import org.junit.Test;

import java.time.Year;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookServiceTest {

    private BookRepository repository = mock(BookRepository.class);

    private BookService service = new BookService(repository);

    private BookResource createBookResource() {
        BookResource resource = new BookResource();

        resource.setTitle("title");
        resource.setAuthor("author");
        resource.setPublicationYear(Year.parse("2000"));

        return resource;
    }

    @Test
    public void shouldRegisterBook() {
        // given
        BookResource resource = createBookResource();
        Book book = new Book(resource.getTitle(), resource.getAuthor(), resource.getPublicationYear());

        when(repository.save(book)).thenReturn(book);

        // when
        Book registered = service.registerBook(resource);

        // then
        assertEquals(resource.getTitle(), registered.getTitle());
        assertEquals(resource.getAuthor(), registered.getAuthor());
        assertEquals(resource.getPublicationYear(), registered.getPublicationYear());
        assertEquals(new HashSet<>(), registered.getEditions());
    }

    @Test
    public void shouldUpdateBook() {
        // given
        BookResource resource = createBookResource();
        Book bookToUpdate = new Book("title2", "author2", Year.parse("2001"));
        Book bookUpdated = new Book(resource.getTitle(), resource.getAuthor(), Year.parse("2001"));
        long bookId = 1;

        when(repository.findOne(bookId)).thenReturn(bookToUpdate);
        when(repository.save(bookToUpdate)).thenReturn(bookUpdated);

        // when
        Book result = service.updateBook(bookId, resource);

        // then
        assertEquals(bookUpdated.getTitle(), result.getTitle());
        assertEquals(bookUpdated.getAuthor(), result.getAuthor());
        assertEquals(bookUpdated.getPublicationYear(), result.getPublicationYear());
    }

    @Test
    public void shouldFindBookById() {
        // given
        Book book = new Book("title", "author", Year.parse("2000"));
        long bookId = 1;

        when(repository.findOne(bookId)).thenReturn(book);

        // when
        Book result = service.findBookById(bookId);

        // then
        assertEquals(book.getTitle(), result.getTitle());
        assertEquals(book.getAuthor(), result.getAuthor());
        assertEquals(book.getPublicationYear(), result.getPublicationYear());
    }

    @Test
    public void shouldFindBooksByTitleAndAuthor() {
        // given
        Book book1 = new Book("title", "author", Year.parse("2000"));
        Book book2 = new Book("title2", "author2", Year.parse("2001"));
        String title = "titl";
        String author = "author";

        List<Book> foundBooks = Arrays.asList(book1, book2);

        when(repository.findBooks(title, author)).thenReturn(foundBooks);

        // when
        List<Book> result = service.findBooks(title, author);

        // then
        assertEquals(2, result.size());
        assertEquals(book1.getTitle(), result.get(0).getTitle());
        assertEquals(book1.getAuthor(), result.get(0).getAuthor());
        assertEquals(book1.getPublicationYear(), result.get(0).getPublicationYear());
        assertEquals(book2.getTitle(), result.get(1).getTitle());
        assertEquals(book2.getAuthor(), result.get(1).getAuthor());
        assertEquals(book2.getPublicationYear(), result.get(1).getPublicationYear());
    }

    @Test
    public void shouldFindBooksByTitle() {
        // given
        Book book1 = new Book("title", "author", Year.parse("2000"));
        Book book2 = new Book("title2", "someone", Year.parse("2001"));
        String title = "title";
        String author = null;

        List<Book> foundBooks = Arrays.asList(book1, book2);

        when(repository.findBooks(title, author)).thenReturn(foundBooks);

        // when
        List<Book> result = service.findBooks(title, author);

        // then
        assertEquals(2, result.size());
        assertEquals(book1.getTitle(), result.get(0).getTitle());
        assertEquals(book1.getAuthor(), result.get(0).getAuthor());
        assertEquals(book1.getPublicationYear(), result.get(0).getPublicationYear());
        assertEquals(book2.getTitle(), result.get(1).getTitle());
        assertEquals(book2.getAuthor(), result.get(1).getAuthor());
        assertEquals(book2.getPublicationYear(), result.get(1).getPublicationYear());
    }

    @Test
    public void bookExistsShouldReturnTrueIfSuchBookExists() {
        // given
        BookResource resource = createBookResource();

        when(repository.existsByTitleAndAuthor(resource.getTitle(), resource.getAuthor()))
                .thenReturn(true);

        // when
        boolean result = service.bookExists(resource);

        // then
        assertTrue(result);
    }

    @Test
    public void bookExistsReturnFalseTrueIfSuchBookNotExists() {
        // given
        BookResource resource = createBookResource();

        when(repository.existsByTitleAndAuthor(resource.getTitle(), resource.getAuthor()))
                .thenReturn(false);

        // when
        boolean result = service.bookExists(resource);

        // then
        assertFalse(result);
    }

    @Test
    public void bookExistsShouldReturnFalse() {
        // given
        long bookId = 1;

        when(repository.exists(bookId)).thenReturn(false);

        // when
        boolean result = service.bookExists(bookId);

        // then
        assertFalse(result);
    }

}