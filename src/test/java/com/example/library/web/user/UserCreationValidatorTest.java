package com.example.library.web.user;

import com.example.library.domain.user.UserService;
import com.example.library.web.ErrorsResource;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserCreationValidatorTest {

    private UserService service = mock(UserService.class);
    private UserCreationValidator validator = new UserCreationValidator(service);

    @Test
    public void shouldValidateWithNoErrors() {
        // given
        UserResource resource = createResource("First", "Last");

        when(service.userExists(resource)).thenReturn(false);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertTrue(errorsResource.getValidationErrors().isEmpty());
    }

    @Test
    public void shouldHaveErrorWhenFirstNameIsEmpty() {
        // given
        UserResource resource = createResource("", "Last");

        when(service.userExists(resource)).thenReturn(false);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertEquals(1, errorsResource.getValidationErrors().size());
        assertTrue(errorsResource.getValidationErrors().contains("First name not specified"));
    }

    @Test
    public void shouldHaveErrorWhenFirstNameIsNull() {
        // given
        UserResource resource = createResource(null, "Last");

        when(service.userExists(resource)).thenReturn(false);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertEquals(1, errorsResource.getValidationErrors().size());
        assertTrue(errorsResource.getValidationErrors().contains("First name not specified"));
    }

    @Test
    public void shouldHaveErrorWhenLastNameIsEmpty() {
        // given
        UserResource resource = createResource("First", "");

        when(service.userExists(resource)).thenReturn(false);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertEquals(1, errorsResource.getValidationErrors().size());
        assertTrue(errorsResource.getValidationErrors().contains("Last name not specified"));
    }

    @Test
    public void shouldHaveErrorWhenLastNameIsNull() {
        // given
        UserResource resource = createResource("First", null);

        when(service.userExists(resource)).thenReturn(false);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertEquals(1, errorsResource.getValidationErrors().size());
        assertTrue(errorsResource.getValidationErrors().contains("Last name not specified"));
    }

    @Test
    public void shouldHaveErrorWhenUserAlreadyExists() {
        // given
        UserResource resource = createResource("First", "Last");

        when(service.userExists(resource)).thenReturn(true);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertEquals(1, errorsResource.getValidationErrors().size());
        assertTrue(errorsResource.getValidationErrors().contains("User already exists"));
    }

    @Test
    public void shouldMultipleErrorsIfNamesNotSpecifiedAndUserExists() {
        // given
        UserResource resource = new UserResource();

        when(service.userExists(resource)).thenReturn(true);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertEquals(2, errorsResource.getValidationErrors().size());
        assertTrue(errorsResource.getValidationErrors().contains("Last name not specified"));
        assertTrue(errorsResource.getValidationErrors().contains("First name not specified"));
    }

    private UserResource createResource(String first, String lastName) {
        UserResource resource = new UserResource();

        resource.setFirstName(first);
        resource.setLastName(lastName);

        return resource;
    }
}