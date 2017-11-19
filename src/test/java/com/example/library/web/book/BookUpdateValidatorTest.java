package com.example.library.web.book;

import com.example.library.web.ErrorsResource;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BookUpdateValidatorTest {

    private BookUpdateValidator validator = new BookUpdateValidator();

    private BookResource createBookResource(String title, String author) {
        BookResource resource = new BookResource();

        resource.setTitle(title);
        resource.setAuthor(author);

        return resource;
    }

    @Test
    public void shouldNotHaveErrors() {
        // given
        BookResource resource = createBookResource("title", "author");

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertTrue(errorsResource.getValidationErrors().isEmpty());
    }

    @Test
    public void shouldHaveErrorIfTitleIsNull() {
        // given
        BookResource resource = createBookResource(null, "author");

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertEquals(1, errorsResource.getValidationErrors().size());
        assertEquals("Title not specified", errorsResource.getValidationErrors().get(0));
    }

    @Test
    public void shouldHaveErrorIfAuthorIsNull() {
        // given
        BookResource resource = createBookResource("title", null);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertEquals(1, errorsResource.getValidationErrors().size());
        assertEquals("Author not specified", errorsResource.getValidationErrors().get(0));
    }

    @Test
    public void shouldHaveErrorsIfTitleAndAuthorIsNull() {
        // given
        BookResource resource = createBookResource(null, null);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertEquals(2, errorsResource.getValidationErrors().size());
        assertTrue(errorsResource.getValidationErrors().contains("Author not specified"));
        assertTrue(errorsResource.getValidationErrors().contains("Title not specified"));
    }

}