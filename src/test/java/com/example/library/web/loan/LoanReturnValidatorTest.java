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

    private LoanResource getLoanResourceMock(long userId, long editionId, long loanId) {
        LoanResource resource = mock(LoanResource.class);

        when(resource.getId()).thenReturn(loanId);
        when(resource.getUserId()).thenReturn(userId);
        when(resource.getEditionId()).thenReturn(editionId);

        return resource;
    }

    @Test
    public void shouldValidateWithNoErrors() {
        // given
        long userId = 1;
        long editionId = 1;
        long loanId = 1;
        LoanResource resource = getLoanResourceMock(userId, editionId, loanId);

        when(loanService.canBeReturned(loanId)).thenReturn(true);
        when(userService.canReturn(userId)).thenReturn(true);
        when(editionService.canBeReturned(editionId)).thenReturn(true);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertTrue(result.getValidationErrors().isEmpty());
    }

    @Test
    public void shouldValidateWithNoErrorIfLoanWasReturned() {
        // given
        long userId = 1;
        long editionId = 1;
        long loanId = 1;
        LoanResource resource = getLoanResourceMock(userId, editionId, loanId);

        when(loanService.canBeReturned(loanId)).thenReturn(false);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertEquals(1, result.getValidationErrors().size());
        assertTrue(result.getValidationErrors().contains("Loan has been returned"));
    }

    @Test
    public void shouldValidateWithNoErrorIfUserDidNotBorrow() {
        // given
        long userId = 1;
        long editionId = 1;
        long loanId = 1;
        LoanResource resource = getLoanResourceMock(userId, editionId, loanId);

        when(loanService.canBeReturned(loanId)).thenReturn(true);
        when(userService.canReturn(userId)).thenReturn(false);
        when(editionService.canBeReturned(editionId)).thenReturn(true);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertEquals(1, result.getValidationErrors().size());
        assertTrue(result.getValidationErrors().contains("User has no borrowed books"));
    }

    @Test
    public void shouldValidateWithNoErrorIfEditionWasNotLoaned() {
        // given
        long userId = 1;
        long editionId = 1;
        long loanId = 1;
        LoanResource resource = getLoanResourceMock(userId, editionId, loanId);

        when(loanService.canBeReturned(loanId)).thenReturn(true);
        when(userService.canReturn(userId)).thenReturn(true);
        when(editionService.canBeReturned(editionId)).thenReturn(false);

        // when
        ErrorsResource result = validator.validate(resource);

        // then
        assertEquals(1, result.getValidationErrors().size());
        assertTrue(result.getValidationErrors().contains("Edition has not been loaned"));
    }

}