package com.example.library.web.edition;

import com.example.library.domain.edition.EditionService;
import com.example.library.web.ErrorsResource;
import org.junit.Test;

import java.time.Year;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EditionCreationValidatorTest {

    private EditionService service = mock(EditionService.class);
    private EditionCreationValidator validator = new EditionCreationValidator(service);

    @Test
    public void shouldValidateWithNoErrors() {
        // given
        EditionResource resource = new EditionResource();
        resource.setIsbn("1234567890123");
        resource.setQuantity(5);
        resource.setPublicationYear(Year.parse("2000"));

        when(service.hasNoSuchIsbn(resource.getIsbn())).thenReturn(true);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertTrue(errorsResource.getValidationErrors().isEmpty());
    }

    @Test
    public void shouldValidateWithErrorIfIsbnIsEmptyOrNull() {
        // given
        EditionResource resource = new EditionResource();
        resource.setQuantity(5);
        resource.setPublicationYear(Year.parse("2000"));

        when(service.hasNoSuchIsbn(resource.getIsbn())).thenReturn(true);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertEquals(1, errorsResource.getValidationErrors().size());
        assertTrue(errorsResource.getValidationErrors().contains("Isbn not specified"));
    }

    @Test
    public void shouldValidateWithErrorIfIsbnHasWrongFormat() {
        // given
        EditionResource resource = new EditionResource();
        resource.setIsbn("12345678901");
        resource.setQuantity(5);
        resource.setPublicationYear(Year.parse("2000"));

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertEquals(1, errorsResource.getValidationErrors().size());
        assertTrue(errorsResource.getValidationErrors().contains("Wrong isbn format"));
    }

    @Test
    public void shouldValidateWithErrorIfIsbnAlreadyExists() {
        // given
        EditionResource resource = new EditionResource();
        resource.setIsbn("1234567890123");
        resource.setQuantity(5);
        resource.setPublicationYear(Year.parse("2000"));

        when(service.hasNoSuchIsbn(resource.getIsbn())).thenReturn(false);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertEquals(1, errorsResource.getValidationErrors().size());
        assertTrue(errorsResource.getValidationErrors().contains("Isbn already exists"));
    }

    @Test
    public void shouldValidateWithErrorIfPublicationYearIsNull() {
        // given
        EditionResource resource = new EditionResource();
        resource.setIsbn("1234567890123");
        resource.setQuantity(5);

        when(service.hasNoSuchIsbn(resource.getIsbn())).thenReturn(true);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertEquals(1, errorsResource.getValidationErrors().size());
        assertTrue(errorsResource.getValidationErrors().contains("Publication year not specified"));
    }

    @Test
    public void shouldValidateWithErrorIfQuantityIsLessThanZero() {
        // given
        EditionResource resource = new EditionResource();
        resource.setIsbn("1234567890123");
        resource.setQuantity(-5);
        resource.setPublicationYear(Year.parse("2000"));

        when(service.hasNoSuchIsbn(resource.getIsbn())).thenReturn(true);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertEquals(1, errorsResource.getValidationErrors().size());
        assertTrue(errorsResource.getValidationErrors().contains("Wrong quantity"));
    }

    @Test
    public void shouldValidateWithErrors() {
        // given
        EditionResource resource = new EditionResource();
        resource.setIsbn("1234567890123");
        resource.setQuantity(-5);

        when(service.hasNoSuchIsbn(resource.getIsbn())).thenReturn(false);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertEquals(3, errorsResource.getValidationErrors().size());
        assertTrue(errorsResource.getValidationErrors().contains("Isbn already exists"));
        assertTrue(errorsResource.getValidationErrors().contains("Wrong quantity"));
        assertTrue(errorsResource.getValidationErrors().contains("Publication year not specified"));
    }

}