package com.example.library.web.book;

import com.example.library.domain.book.BookService;
import com.example.library.web.ErrorsResource;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookCreationValidatorTest {

    private BookService service = mock(BookService.class);
    private BookCreationValidator validator = new BookCreationValidator(service);

    @Test
    public void shouldNotHaveErrors() {
        // given
        BookResource resource = new BookResource();
        resource.setTitle("title");
        resource.setAuthor("author");

        when(service.hasNoSuchBook(resource)).thenReturn(true);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertTrue(errorsResource.getValidationErrors().isEmpty());
    }

    @Test
    public void shouldHaveErrorIfTitleIsNull() {
        // given
        BookResource resource = new BookResource();
        resource.setAuthor("author");

        when(service.hasNoSuchBook(resource)).thenReturn(true);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertTrue(errorsResource.getValidationErrors().contains("Title not specified"));
    }

    @Test
    public void shouldHaveErrorIfAuthorIsNull() {
        // given
        BookResource resource = new BookResource();
        resource.setTitle("title");

        when(service.hasNoSuchBook(resource)).thenReturn(true);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertTrue(errorsResource.getValidationErrors().contains("Author not specified"));
    }

    @Test
    public void shouldHaveErrorWhenBookExists() {
        // given
        BookResource resource = new BookResource();
        resource.setTitle("title");
        resource.setAuthor("author");

        when(service.hasNoSuchBook(resource)).thenReturn(false);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertTrue(errorsResource.getValidationErrors().contains("Book already exists"));
    }

    @Test
    public void shouldHaveMultipleErrors() {
        // given
        BookResource resource = new BookResource();

        when(service.hasNoSuchBook(resource)).thenReturn(false);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertTrue(errorsResource.getValidationErrors().contains("Author not specified"));
        assertTrue(errorsResource.getValidationErrors().contains("Title not specified"));
    }
}