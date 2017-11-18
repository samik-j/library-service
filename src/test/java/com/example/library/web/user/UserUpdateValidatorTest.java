package com.example.library.web.user;

import com.example.library.web.ErrorsResource;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class UserUpdateValidatorTest {

    private UserUpdateValidator validator = new UserUpdateValidator();

    @Test
    public void shouldValidateWithNoErrors() {
        // given
        UserResource resource = new UserResource();
        resource.setLastName("Last");

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertTrue(result.getValidationErrors().isEmpty());
    }

    @Test
    public void shouldValidateWithErrorIfLastNameIsNull() {
        // given
        UserResource resource = new UserResource();

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertTrue(result.getValidationErrors().contains("Last name not specified"));
    }

    @Test
    public void shouldValidateWithErrorIfLastNameIsEmpty() {
        // given
        UserResource resource = new UserResource();
        resource.setLastName("");

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertTrue(result.getValidationErrors().contains("Last name not specified"));
    }

}