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

    private LoanResource createLoanResource(Long userId, Long editionId) {
        LoanResource resource = new LoanResource();

        resource.setUserId(userId);
        resource.setEditionId(editionId);

        return resource;
    }

    @Test
    public void shouldValidateWithNoErrors() {
        // given
        LoanResource resource = createLoanResource(1L, 1L);

        when(userService.userExists(resource.getUserId())).thenReturn(true);
        when(editionService.editionExists(resource.getEditionId())).thenReturn(true);
        when(userService.canBorrow(resource.getUserId())).thenReturn(true);
        when(editionService.canBeLend(resource.getEditionId())).thenReturn(true);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertTrue(result.getValidationErrors().isEmpty());
    }

    @Test
    public void shouldValidateWithErrorIfUserIdIsNull() {
        // given
        LoanResource resource = createLoanResource(null, 1L);

        when(editionService.editionExists(resource.getEditionId())).thenReturn(true);
        when(editionService.canBeLend(resource.getEditionId())).thenReturn(true);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertEquals(1, result.getValidationErrors().size());
        assertTrue(result.getValidationErrors().contains("User id not specified"));
    }

    @Test
    public void shouldValidateWithErrorIfUserDoesNotExist() {
        // given
        LoanResource resource = createLoanResource(1L, 1L);

        when(userService.userExists(resource.getUserId())).thenReturn(false);
        when(editionService.editionExists(resource.getEditionId())).thenReturn(true);
        when(editionService.canBeLend(resource.getEditionId())).thenReturn(true);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertEquals(1, result.getValidationErrors().size());
        assertTrue(result.getValidationErrors().contains("User does not exist"));
    }

    @Test
    public void shouldValidateWithErrorIfUserCanNotBorrow() {
        // given
        LoanResource resource = createLoanResource(1L, 1L);

        when(userService.userExists(resource.getUserId())).thenReturn(true);
        when(userService.canBorrow(resource.getUserId())).thenReturn(false);
        when(editionService.editionExists(resource.getEditionId())).thenReturn(true);
        when(editionService.canBeLend(resource.getEditionId())).thenReturn(true);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertEquals(1, result.getValidationErrors().size());
        assertTrue(result.getValidationErrors().contains("Not possible to loan because user has reached borrowing limit"));
    }

    @Test
    public void shouldValidateWithErrorIfEditionIdIsNull() {
        // given
        LoanResource resource = createLoanResource(1L, null);

        when(userService.userExists(resource.getUserId())).thenReturn(true);
        when(userService.canBorrow(resource.getUserId())).thenReturn(true);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertEquals(1, result.getValidationErrors().size());
        assertTrue(result.getValidationErrors().contains("Edition id not specified"));
    }

    @Test
    public void shouldValidateWithErrorIfEditionDoesNotExist() {
        // given
        LoanResource resource = createLoanResource(1L, 1L);

        when(userService.userExists(resource.getUserId())).thenReturn(true);
        when(userService.canBorrow(resource.getUserId())).thenReturn(true);
        when(editionService.editionExists(resource.getEditionId())).thenReturn(false);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertEquals(1, result.getValidationErrors().size());
        assertTrue(result.getValidationErrors().contains("Edition does not exist"));
    }

    @Test
    public void shouldValidateWithErrorIfEditionCanNotBeLend() {
        // given
        LoanResource resource = createLoanResource(1L, 1L);

        when(userService.userExists(resource.getUserId())).thenReturn(true);
        when(userService.canBorrow(resource.getUserId())).thenReturn(true);
        when(editionService.editionExists(resource.getEditionId())).thenReturn(true);
        when(editionService.canBeLend(resource.getEditionId())).thenReturn(false);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertEquals(1, result.getValidationErrors().size());
        assertTrue(result.getValidationErrors().contains("Not possible to loan because of insufficient quantity available"));
    }

    @Test
    public void shouldValidateWithErrors() {
        // given
        LoanResource resource = createLoanResource(1L, 1L);

        when(userService.userExists(resource.getUserId())).thenReturn(true);
        when(userService.canBorrow(resource.getUserId())).thenReturn(false);
        when(editionService.editionExists(resource.getEditionId())).thenReturn(false);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertEquals(2, result.getValidationErrors().size());
        assertTrue(result.getValidationErrors().contains("Not possible to loan because user has reached borrowing limit"));
        assertTrue(result.getValidationErrors().contains("Edition does not exist"));
    }

}