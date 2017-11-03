package com.example.library.domain.loan;

import com.example.library.domain.book.Book;
import com.example.library.domain.edition.Edition;
import com.example.library.domain.user.User;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Year;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;


public class LoanTest {

    @Test
    public void shouldCreateLoan() {
        // given
        Book book = new Book("title", "author", Year.parse("2000"));
        User user = new User("Filip", "Karas");
        Edition edition = new Edition("12342", Year.parse("2000"), 5, book);

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
        Book book = new Book("title", "author", Year.parse("2000"));
        User user = new User("Filip", "Karas");
        Edition edition = new Edition("12342", Year.parse("2000"), 5, book);

        // when
        Loan loan = new Loan(user, edition);

        // then
        assertFalse(loan.isOverdue());
    }

}