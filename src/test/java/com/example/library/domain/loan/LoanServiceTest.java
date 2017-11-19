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

    @Test
    public void shouldRegisterLoan() {
        // given
        long userId = 1;
        long editionId = 1;
        Edition edition = createEdition(editionId);
        User user = createUser(userId);
        LoanResource resource = createLoanResource(userId, editionId);
        Loan registered = new Loan(user, edition);

        when(editionRepository.findOne(editionId)).thenReturn(edition);
        when(userRepository.findOne(userId)).thenReturn(user);
        when(editionRepository.save(edition)).thenReturn(edition);
        when(userRepository.save(user)).thenReturn(user);
        when(loanRepository.save(registered)).thenReturn(registered);

        // when
        Loan result = service.registerLoan(resource);

        // then
        verify(edition, times(1)).lend();
        verify(user, times(1)).borrow();
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
        Edition edition = createEdition(editionId);
        User user = createUser(userId);
        Loan loan = new Loan(user, edition);
        Loan loanReturned = createReturnedLoan(loanId);

        when(loanRepository.findOne(loanId)).thenReturn(loan);
        when(editionRepository.findOne(editionId)).thenReturn(edition);
        when(userRepository.findOne(userId)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(editionRepository.save(edition)).thenReturn(edition);
        when(loanRepository.save(loan)).thenReturn(loanReturned);

        // when
        Loan result = service.returnLoan(loanId);

        // then
        verify(edition, times(1)).returnEdition();
        verify(user, times(1)).returnEdition();
        assertTrue(result.isReturned());
    }

    @Test
    public void shouldFindAllLoans() {
        // given
        Loan loan = createLoan(1);
        List<Loan> loans = Arrays.asList(loan);

        when(loanRepository.findAll()).thenReturn(loans);

        // when
        List<Loan> result = service.findLoans();

        // then
        assertEquals(1, result.size());
        assertTrue(result.contains(loan));
        assertEquals(loan.getId(), result.get(0).getId());
        assertEquals(loan.getUser(), result.get(0).getUser());
        assertEquals(loan.getEdition(), result.get(0).getEdition());
        assertEquals(loan.getDateLent(), result.get(0).getDateLent());
        assertEquals(loan.getDateToReturn(), result.get(0).getDateToReturn());
        assertEquals(loan.isReturned(), result.get(0).isReturned());
    }

    @Test
    public void shouldFindLoansOverdue() {
        // given
        Loan loan = createOverdueLoan(1);
        List<Loan> loans = Arrays.asList(loan);

        when(loanRepository.findByDateToReturnBefore(LocalDate.now())).thenReturn(loans);

        // when
        List<Loan> result = service.findLoans(LoanOverdue.NOW);

        // then
        assertEquals(1, result.size());
        assertTrue(result.contains(loan));
        assertEquals(loan.getId(), result.get(0).getId());
        assertEquals(loan.getUser(), result.get(0).getUser());
        assertEquals(loan.getEdition(), result.get(0).getEdition());
        assertEquals(loan.getDateLent(), result.get(0).getDateLent());
        assertEquals(loan.getDateToReturn(), result.get(0).getDateToReturn());
        assertEquals(loan.isReturned(), result.get(0).isReturned());
    }

    @Test
    public void shouldFindLoansReturned() {
        // given
        Loan loan = createReturnedLoan(1);
        List<Loan> loans = Arrays.asList(loan);

        boolean returned = true;

        when(loanRepository.findByReturned(returned)).thenReturn(loans);

        // when
        List<Loan> result = service.findLoans(returned);

        // then
        assertEquals(1, result.size());
        assertTrue(result.contains(loan));
        assertEquals(loan.getId(), result.get(0).getId());
        assertEquals(loan.getUser(), result.get(0).getUser());
        assertEquals(loan.getEdition(), result.get(0).getEdition());
        assertEquals(loan.getDateLent(), result.get(0).getDateLent());
        assertEquals(loan.getDateToReturn(), result.get(0).getDateToReturn());
        assertEquals(loan.isReturned(), result.get(0).isReturned());
    }

    @Test
    public void shouldFindLoansByBookId() {
        // given
        Loan loan = createLoan(1);
        List<Loan> loans = Arrays.asList(loan);

        long bookId = 1;

        when(loanRepository.findByBookId(bookId)).thenReturn(loans);

        // when
        List<Loan> result = service.findLoans(bookId);

        // then
        assertEquals(1, result.size());
        assertTrue(result.contains(loan));
        assertEquals(loan.getId(), result.get(0).getId());
        assertEquals(loan.getUser(), result.get(0).getUser());
        assertEquals(loan.getEdition(), result.get(0).getEdition());
        assertEquals(loan.getDateLent(), result.get(0).getDateLent());
        assertEquals(loan.getDateToReturn(), result.get(0).getDateToReturn());
        assertEquals(loan.isReturned(), result.get(0).isReturned());
    }

    @Test
    public void shouldFindLoanById() {
        // given
        long loanId = 1;
        Loan loan = createLoan(loanId);

        when(loanRepository.findOne(loanId)).thenReturn(loan);

        // when
        Loan result = service.findLoan(loanId);

        // then
        assertEquals(loan.getId(), result.getId());
        assertEquals(loan.getUser(), result.getUser());
        assertEquals(loan.getEdition(), result.getEdition());
        assertEquals(loan.getDateLent(), result.getDateLent());
        assertEquals(loan.getDateToReturn(), result.getDateToReturn());
        assertEquals(loan.isReturned(), result.isReturned());
    }

    @Test
    public void caBeReturnedShouldReturnFalse() {
        // given
        long loanId = 1;
        Loan loan = createReturnedLoan(1);

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

    private LoanResource createLoanResource(long userId, long editionId) {
        LoanResource resource = new LoanResource();

        resource.setUserId(userId);
        resource.setEditionId(editionId);

        return resource;
    }

    private User createUser(long userId) {
        User user = mock(User.class);

        when(user.getId()).thenReturn(userId);

        return user;
    }

    private Edition createEdition(long editionId) {
        Edition edition = mock(Edition.class);

        when(edition.getId()).thenReturn(editionId);

        return edition;
    }

    private Loan createLoan(long loanId) {
        Loan loan = mock(Loan.class);
        User user = createUser(1);
        Edition edition = createEdition(1);

        when(loan.getId()).thenReturn(loanId);
        when(loan.getUser()).thenReturn(user);
        when(loan.getEdition()).thenReturn(edition);
        when(loan.getDateLent()).thenReturn(LocalDate.now());
        when(loan.getDateToReturn()).thenReturn(LocalDate.now().plusDays(14));
        when(loan.isReturned()).thenReturn(false);

        return loan;
    }

    private Loan createReturnedLoan(long loanId) {
        Loan loan = mock(Loan.class);
        User user = createUser(1);
        Edition edition = createEdition(1);

        when(loan.getId()).thenReturn(loanId);
        when(loan.getUser()).thenReturn(user);
        when(loan.getEdition()).thenReturn(edition);
        when(loan.getDateLent()).thenReturn(LocalDate.now());
        when(loan.getDateToReturn()).thenReturn(LocalDate.now().plusDays(14));
        when(loan.isReturned()).thenReturn(true);

        return loan;
    }

    private Loan createOverdueLoan(long loanId) {
        Loan loan = mock(Loan.class);
        User user = createUser(1);
        Edition edition = createEdition(1);

        when(loan.getId()).thenReturn(loanId);
        when(loan.getUser()).thenReturn(user);
        when(loan.getEdition()).thenReturn(edition);
        when(loan.getDateLent()).thenReturn(LocalDate.now());
        when(loan.getDateToReturn()).thenReturn(LocalDate.now().plusDays(14));
        when(loan.isReturned()).thenReturn(false);
        when(loan.isOverdue()).thenReturn(true);

        return loan;
    }

}