package com.example.library.domain.book;

import com.example.library.domain.edition.Edition;
import org.junit.Test;

import java.time.Year;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BookTest {

    @Test
    public void shouldCreateBook() {
        // when
        Book book = new Book("title", "author", Year.parse("2000"));

        // then
        assertEquals("title", book.getTitle());
        assertEquals("author", book.getAuthor());
        assertEquals(Year.parse("2000"), book.getPublicationYear());
        assertEquals(new HashSet<>(), book.getEditions());

        assertTrue(book.getEditions().isEmpty());
        assertEquals(0, book.getQuantity());
        assertEquals(0, book.getNumberOfEditions());
        assertEquals(0, book.getOnLoanQuantity());
    }

    @Test
    public void shouldUpdateAuthor() {
        // given
        Book book = new Book("title", "author", Year.parse("2000"));

        // when
        book.updateAuthor("author2");

        // then
        assertEquals("author2", book.getAuthor());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateAuthorShouldThrowExceptionWhenParameterIsNull() {
        // given
        Book book = new Book("title", "author", Year.parse("2000"));

        // when
        book.updateAuthor(null);
    }

    @Test
    public void shouldUpdateTitle() {
        // given
        Book book = new Book("title", "author", Year.parse("2000"));

        // when
        book.updateTitle("title2");

        // then
        assertEquals("title2", book.getTitle());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateTitleShouldThrowExceptionWhenParameterIsNull() {
        // given
        Book book = new Book("title", "author", Year.parse("2000"));

        // when
        book.updateTitle(null);
    }

    @Test
    public void shouldAddEditionAndReturnQuantities() {
        // given
        Book book = new Book("title", "author", Year.parse("2000"));

        // when
        book.addEdition(new Edition("1234567890123", Year.parse("2000"), 5, book));

        // then
        assertEquals(5, book.getQuantity());
        assertEquals(1, book.getNumberOfEditions());
        assertEquals(0, book.getOnLoanQuantity());
    }
}