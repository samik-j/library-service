package com.example.library.web.loan;


import com.example.library.domain.edition.EditionService;
import com.example.library.domain.user.UserService;
import com.example.library.web.ErrorsResource;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoanCreationValidatorTest {

    private UserService userService = mock(UserService.class);
    private EditionService editionService = mock(EditionService.class);
    private LoanCreationValidator validator = new LoanCreationValidator(userService, editionService);

    private LoanResource getLoanResource(Long userId, Long editionId) {
        LoanResource resource = new LoanResource();

        resource.setUserId(userId);
        resource.setEditionId(editionId);

        return resource;
    }

    @Test
    public void shouldValidateWithNoErrors() {
        // given
        Long userId = 1L;
        Long editionId = 1L;

        LoanResource resource = getLoanResource(userId, editionId);

        when(userService.userExists(userId)).thenReturn(true);
        when(editionService.editionExists(editionId)).thenReturn(true);
        when(userService.canBorrow(userId)).thenReturn(true);
        when(editionService.canBeLend(editionId)).thenReturn(true);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertTrue(result.getValidationErrors().isEmpty());
    }

    @Test
    public void shouldValidateWithErrorIfUserIdIsNull() {
        // given
        Long userId = null;
        Long editionId = 1L;

        LoanResource resource = getLoanResource(userId, editionId);

        when(editionService.editionExists(editionId)).thenReturn(true);
        when(editionService.canBeLend(editionId)).thenReturn(true);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertEquals(1, result.getValidationErrors().size());
        assertTrue(result.getValidationErrors().contains("User id not specified"));
    }

    @Test
    public void shouldValidateWithErrorIfEditionIdIsNull() {
        // given
        Long userId = 1L;
        Long editionId = null;

        LoanResource resource = getLoanResource(userId, editionId);

        when(userService.userExists(userId)).thenReturn(true);
        when(userService.canBorrow(userId)).thenReturn(true);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertEquals(1, result.getValidationErrors().size());
        assertTrue(result.getValidationErrors().contains("Edition id not specified"));
    }

    @Test
    public void shouldValidateWithErrorIfUserDoesNotExist() {
        // given
        Long userId = 1L;
        Long editionId = 1L;

        LoanResource resource = getLoanResource(userId, editionId);

        when(userService.userExists(userId)).thenReturn(false);
        when(editionService.editionExists(editionId)).thenReturn(true);
        when(editionService.canBeLend(editionId)).thenReturn(true);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertEquals(1, result.getValidationErrors().size());
        assertTrue(result.getValidationErrors().contains("User does not exist"));
    }

    @Test
    public void shouldValidateWithErrorIfEditionDoesNotExist() {
        // given
        Long userId = 1L;
        Long editionId = 1L;

        LoanResource resource = getLoanResource(userId, editionId);

        when(userService.userExists(userId)).thenReturn(true);
        when(userService.canBorrow(userId)).thenReturn(true);
        when(editionService.editionExists(editionId)).thenReturn(false);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertEquals(1, result.getValidationErrors().size());
        assertTrue(result.getValidationErrors().contains("Edition does not exist"));
    }

    @Test
    public void shouldValidateWithErrorIfUserCanNotBorrow() {
        // given
        Long userId = 1L;
        Long editionId = 1L;

        LoanResource resource = getLoanResource(userId, editionId);

        when(userService.userExists(userId)).thenReturn(true);
        when(userService.canBorrow(userId)).thenReturn(false);
        when(editionService.editionExists(editionId)).thenReturn(true);
        when(editionService.canBeLend(editionId)).thenReturn(true);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertEquals(1, result.getValidationErrors().size());
        assertTrue(result.getValidationErrors().contains("Not possible to loan because user has reached borrowing limit"));
    }

    @Test
    public void shouldValidateWithErrorIfEditionCanNotBeLend() {
        // given
        Long userId = 1L;
        Long editionId = 1L;

        LoanResource resource = getLoanResource(userId, editionId);

        when(userService.userExists(userId)).thenReturn(true);
        when(userService.canBorrow(userId)).thenReturn(true);
        when(editionService.editionExists(editionId)).thenReturn(true);
        when(editionService.canBeLend(editionId)).thenReturn(false);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertEquals(1, result.getValidationErrors().size());
        assertTrue(result.getValidationErrors().contains("Not possible to loan because of insufficient quantity available"));
    }

    @Test
    public void shouldValidateWithErrors() {
        // given
        Long userId = 1L;
        Long editionId = 1L;

        LoanResource resource = getLoanResource(userId, editionId);

        when(userService.userExists(userId)).thenReturn(true);
        when(userService.canBorrow(userId)).thenReturn(false);
        when(editionService.editionExists(editionId)).thenReturn(false);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertEquals(2, result.getValidationErrors().size());
        assertTrue(result.getValidationErrors().contains("Not possible to loan because user has reached borrowing limit"));
        assertTrue(result.getValidationErrors().contains("Edition does not exist"));
    }

}