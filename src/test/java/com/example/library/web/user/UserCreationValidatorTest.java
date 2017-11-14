package com.example.library.web.user;

import com.example.library.domain.user.UserService;
import com.example.library.web.ErrorsResource;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;

public class UserCreationValidatorTest {

    private UserService service = mock(UserService.class);
    private UserCreationValidator validator = new UserCreationValidator(service);

    @Test
    public void shouldValidateWithNoErrors() {
        // given
        UserResource resource = new UserResource();
        resource.setFirstName("First");
        resource.setLastName("Last");

        when(service.hasNoSuchUser(resource)).thenReturn(true);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertTrue(errorsResource.getValidationErrors().isEmpty());
    }

    @Test
    public void shouldHaveErrorWhenFirstNameIsNullOrEmpty() {
        // given
        UserResource resource = new UserResource();
        resource.setFirstName("");
        resource.setLastName("Last");

        when(service.hasNoSuchUser(resource)).thenReturn(true);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertEquals(1, errorsResource.getValidationErrors().size());
        assertTrue(errorsResource.getValidationErrors().contains("First name not specified"));
    }

    @Test
    public void shouldHaveErrorWhenLastNameIsNullOrEmpty() {
        // given
        UserResource resource = new UserResource();
        resource.setFirstName("First");

        when(service.hasNoSuchUser(resource)).thenReturn(true);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertEquals(1, errorsResource.getValidationErrors().size());
        assertTrue(errorsResource.getValidationErrors().contains("Last name not specified"));
    }

    @Test
    public void shouldHaveErrorWhenUserAlreadyExists() {
        // given
        UserResource resource = new UserResource();
        resource.setFirstName("First");
        resource.setLastName("Last");

        when(service.hasNoSuchUser(resource)).thenReturn(false);

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

        when(service.hasNoSuchUser(resource)).thenReturn(false);

        // when
        ErrorsResource errorsResource = validator.validate(resource);

        // then
        assertEquals(2, errorsResource.getValidationErrors().size());
        assertTrue(errorsResource.getValidationErrors().contains("Last name not specified"));
        assertTrue(errorsResource.getValidationErrors().contains("First name not specified"));
    }
}