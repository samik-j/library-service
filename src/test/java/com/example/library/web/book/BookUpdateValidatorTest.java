package com.example.library.web.book;

import com.example.library.web.ErrorsResource;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BookUpdateValidatorTest {

    private BookUpdateValidator validator = new BookUpdateValidator();

    @Test
    public void shouldValidate() {
        // given
        BookResource resource = new BookResource();
        resource.setTitle("title");
        resource.setAuthor("author");

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertTrue(errorsResource.getValidationErrors().isEmpty());
    }

    @Test
    public void shouldNotValidateWhenTitleIsNull() {
        // given
        BookResource resource = new BookResource();
        resource.setAuthor("author");

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertEquals(1, errorsResource.getValidationErrors().size());
        assertEquals("Title not specified", errorsResource.getValidationErrors().get(0));
    }

    @Test
    public void shouldNotValidateWhenAuthorIsNull() {
        // given
        BookResource resource = new BookResource();
        resource.setTitle("title");

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertEquals(1, errorsResource.getValidationErrors().size());
        assertEquals("Author not specified", errorsResource.getValidationErrors().get(0));
    }

    @Test
    public void shouldNotValidateWhenTitleAndAuthorIsNull() {
        // given
        BookResource resource = new BookResource();

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertEquals(2, errorsResource.getValidationErrors().size());
        assertTrue(errorsResource.getValidationErrors().contains("Author not specified"));
        assertTrue(errorsResource.getValidationErrors().contains("Title not specified"));
    }

}