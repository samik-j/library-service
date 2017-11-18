package com.example.library.web.loan;

import com.example.library.domain.edition.EditionService;
import com.example.library.domain.loan.LoanService;
import com.example.library.domain.user.UserService;
import com.example.library.web.ErrorsResource;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoanReturnValidatorTest {

    private UserService userService = mock(UserService.class);
    private EditionService editionService = mock(EditionService.class);
    private LoanService loanService = mock(LoanService.class);
    private LoanReturnValidator validator = new LoanReturnValidator(userService, editionService, loanService);

    private LoanResource createLoanResource() {
        LoanResource resource = new LoanResource();

        resource.setId(1L);
        resource.setUserId(1L);
        resource.setEditionId(1L);

        return resource;
    }

    @Test
    public void shouldValidateWithNoErrors() {
        // given
        LoanResource resource = createLoanResource();

        when(loanService.canBeReturned(resource.getId())).thenReturn(true);
        when(userService.canReturn(resource.getUserId())).thenReturn(true);
        when(editionService.canBeReturned(resource.getEditionId())).thenReturn(true);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertTrue(result.getValidationErrors().isEmpty());
    }

    @Test
    public void shouldValidateWithErrorIfLoanWasReturned() {
        // given
        LoanResource resource = createLoanResource();

        when(loanService.canBeReturned(resource.getId())).thenReturn(false);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertEquals(1, result.getValidationErrors().size());
        assertTrue(result.getValidationErrors().contains("Loan has been returned"));
    }

    @Test
    public void shouldValidateWithErrorIfUserDidNotBorrow() {
        // given
        LoanResource resource = createLoanResource();

        when(loanService.canBeReturned(resource.getId())).thenReturn(true);
        when(userService.canReturn(resource.getUserId())).thenReturn(false);
        when(editionService.canBeReturned(resource.getEditionId())).thenReturn(true);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertEquals(1, result.getValidationErrors().size());
        assertTrue(result.getValidationErrors().contains("User has no borrowed books"));
    }

    @Test
    public void shouldValidateWithErrorIfEditionWasNotLoaned() {
        // given
        LoanResource resource = createLoanResource();

        when(loanService.canBeReturned(resource.getId())).thenReturn(true);
        when(userService.canReturn(resource.getUserId())).thenReturn(true);
        when(editionService.canBeReturned(resource.getEditionId())).thenReturn(false);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertEquals(1, result.getValidationErrors().size());
        assertTrue(result.getValidationErrors().contains("Edition has not been loaned"));
    }

}