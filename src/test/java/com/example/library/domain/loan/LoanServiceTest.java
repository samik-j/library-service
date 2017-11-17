package com.example.library.domain.loan;

import com.example.library.domain.edition.Edition;
import com.example.library.domain.edition.EditionRepository;
import com.example.library.domain.user.User;
import com.example.library.domain.user.UserRepository;
import com.example.library.web.loan.LoanOverdue;
import com.example.library.web.loan.LoanResource;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LoanServiceTest {

    private LoanRepository loanRepository = mock(LoanRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private EditionRepository editionRepository = mock(EditionRepository.class);

    private LoanService service = new LoanService(loanRepository, userRepository, editionRepository);

    private LoanResource getResource(long userId, long editionId) {
        LoanResource resource = new LoanResource();

        resource.setUserId(userId);
        resource.setEditionId(editionId);

        return resource;
    }

    private User getUserMock(long userId) {
        User user = mock(User.class);

        doNothing().when(user).borrow();
        doNothing().when(user).returnEdition();
        when(user.getId()).thenReturn(userId);

        return user;
    }

    private Edition getEditionMock(long editionId) {
        Edition edition = mock(Edition.class);

        doNothing().when(edition).lend();
        doNothing().when(edition).returnEdition();
        when(edition.getId()).thenReturn(editionId);

        return edition;
    }

    private Loan getLoanMock(long loanId) {
        Loan loan = mock(Loan.class);

        doNothing().when(loan).returnLoan();

        return loan;
    }

    private Loan getReturnedLoanMock(long loanId) {
        Loan loan = mock(Loan.class);

        doNothing().when(loan).returnLoan();
        when(loan.isReturned()).thenReturn(true);

        return loan;
    }

    private Loan getOverdueLoanMock(long loanId) {
        Loan loan = mock(Loan.class);

        doNothing().when(loan).returnLoan();
        when(loan.isOverdue()).thenReturn(true);

        return loan;
    }

    @Test
    public void shouldRegisterLoan() {
        // given
        long userId = 1;
        long editionId = 1;
        Edition edition = getEditionMock(editionId);
        User user = getUserMock(userId);
        LoanResource resource = getResource(userId, editionId);
        Loan registered = new Loan(user, edition);

        when(editionRepository.findOne(editionId)).thenReturn(edition);
        when(userRepository.findOne(userId)).thenReturn(user);
        when(editionRepository.save(edition)).thenReturn(edition);
        when(userRepository.save(user)).thenReturn(user);
        when(loanRepository.save(registered)).thenReturn(registered);

        // when
        Loan result = service.registerLoan(resource);

        // then
        assertEquals((long) resource.getUserId(), result.getUser().getId());
        assertEquals((long) resource.getEditionId(), result.getEdition().getId());
        assertFalse(result.isReturned());
        assertFalse(result.isOverdue());
    }

    @Test
    public void shouldReturnLoan() {
        // given
        long loanId = 1;
        long userId = 1;
        long editionId = 1;
        Edition edition = getEditionMock(editionId);
        User user = getUserMock(userId);
        Loan loan = new Loan(user, edition);
        Loan loanReturned = getReturnedLoanMock(loanId);

        when(loanRepository.findOne(loanId)).thenReturn(loan);
        when(editionRepository.findOne(editionId)).thenReturn(edition);
        when(userRepository.findOne(userId)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(editionRepository.save(edition)).thenReturn(edition);
        when(loanRepository.save(loan)).thenReturn(loanReturned);

        // when
        Loan result = service.returnLoan(loanId);

        // then
        assertTrue(result.isReturned());
    }

    @Test
    public void shouldFindAllLoans() {
        // given
        Loan loan1 = getLoanMock(1);
        Loan loan2 = getLoanMock(2);
        List<Loan> loans = Arrays.asList(loan1, loan2);

        when(loanRepository.findAll()).thenReturn(loans);

        // when
        List<Loan> result = service.findLoans();

        // then
        assertEquals(2, result.size());
        assertTrue(result.contains(loan1));
        assertTrue(result.contains(loan2));
    }

    @Test
    public void shouldFindLoansOverdue() {
        // given
        Loan loan = getOverdueLoanMock(1);
        List<Loan> loans = Arrays.asList(loan);

        when(loanRepository.findByDateToReturnBefore(LocalDate.now())).thenReturn(loans);

        // when
        List<Loan> result = service.findLoans(LoanOverdue.NOW);

        // then
        assertEquals(1, result.size());
        assertTrue(result.contains(loan));
    }

    @Test
    public void shouldFindLoansReturned() {
        // given
        Loan loan = getReturnedLoanMock(1);
        List<Loan> loans = Arrays.asList(loan);

        boolean returned = true;

        when(loanRepository.findByReturned(returned)).thenReturn(loans);

        // when
        List<Loan> result = service.findLoans(returned);

        // then
        assertEquals(1, result.size());
        assertTrue(result.contains(loan));
    }

    @Test
    public void shouldFindLoansByBookId() {
        // given
        Loan loan = getLoanMock(1);
        List<Loan> loans = Arrays.asList(loan);

        long bookId = 1;

        when(loanRepository.findByBookId(bookId)).thenReturn(loans);

        // when
        List<Loan> result = service.findLoans(bookId);

        // then
        assertEquals(1, result.size());
        assertTrue(result.contains(loan));
    }

    @Test
    public void shouldFindLoanById() {
        // given
        long loanId = 1;
        Loan loan = getLoanMock(loanId);

        when(loanRepository.findOne(loanId)).thenReturn(loan);

        // when
        Loan result = service.findLoan(loanId);

        // then
        assertEquals(loan, result);
    }

    @Test
    public void caBeReturnedShouldReturnFalse() {
        // given
        long loanId = 1;
        Loan loan = getReturnedLoanMock(loanId);

        when(loanRepository.findOne(loanId)).thenReturn(loan);

        // when
        boolean result = service.canBeReturned(loanId);

        // then
        assertFalse(result);
    }

    @Test
    public void loanExistsShouldReturnTrue() {
        // given
        long loanId = 1;

        when(loanRepository.exists(loanId)).thenReturn(true);

        // when
        boolean result = service.loanExists(loanId);

        // then
        assertTrue(result);
    }

}