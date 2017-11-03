package com.example.library.domain;

import org.junit.Test;

import java.time.LocalDate;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;


public class LoanTest {

    @Test
    public void shouldCreateLoan() {
        // given
        User user = new User();
        Edition edition = new Edition();

        // when
        Loan loan = new Loan(user, edition);

        // then
        assertEquals(user, loan.getUser());
        assertEquals(edition, loan.getEdition());
        assertEquals(LocalDate.now(), loan.getDateLent());
        assertEquals(LocalDate.now().plusDays(14), loan.getDateToReturn());
    }

    @Test
    public void shouldNotBeOverdueAfterCreation() {
        // given
        User user = new User();
        Edition edition = new Edition();

        // when
        Loan loan = new Loan(user, edition);

        // then
        assertFalse(loan.isOverdue());
    }

}