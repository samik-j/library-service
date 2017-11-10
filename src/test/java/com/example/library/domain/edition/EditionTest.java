package com.example.library.domain.edition;

import com.example.library.domain.InsufficientQuantityToDecreaseException;
import com.example.library.domain.book.Book;
import org.junit.Test;

import java.time.Year;

import static org.junit.Assert.assertEquals;

public class EditionTest {

    @Test
    public void shouldCreateEdition() {
        // given
        Book book = new Book("title", "author", Year.parse("2000"));

        // when
        Edition edition = new Edition("1234567890123", Year.parse("2000"), 2, book);

        //then
        assertEquals("1234567890123", edition.getIsbn());
        assertEquals(Year.parse("2000"), edition.getPublicationYear());
        assertEquals(book, edition.getBook());
        assertEquals(2, edition.getQuantity());
        assertEquals(0, edition.getOnLoan());
    }

    @Test
    public void shouldLend() {
        // given
        Edition edition = createEdition();

        // when
        edition.lend();

        // then
        assertEquals(1, edition.getOnLoan());
    }

    @Test(expected = InsufficientEditionQuantityException.class)
    public void LendShouldNotIncreaseOnLoanAndThrowExceptionWhenOnLoanEqualsQuantity() {
        // given
        Edition edition = createEdition();
        edition.lend();
        edition.lend();

        // when
        edition.lend();
    }

    @Test
    public void shouldReturnEdition() {
        // given
        Edition edition = createEdition();
        edition.lend();

        // when
        edition.returnEdition();

        // then
        assertEquals(0, edition.getOnLoan());
    }

    @Test(expected = InsufficientQuantityToDecreaseException.class)
    public void ReturnLoanShouldNotDecreaseOnLoanAndThrowExceptionWhenOnLoanEqualsZero() {
        // given
        Edition edition = createEdition();

        // when
        edition.returnEdition();
    }

    private Edition createEdition() {
        Book book = new Book("title", "author", Year.parse("2000"));

        return new Edition("1234567890123", Year.parse("2000"), 2, book);
    }
}