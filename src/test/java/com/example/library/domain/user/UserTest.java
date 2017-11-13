package com.example.library.domain.user;

import com.example.library.domain.InsufficientQuantityToDecreaseException;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class UserTest {

    @Test
    public void shouldCreateUser() {
        // when
        User user = new User("FirstName", "LastName");

        // then
        assertEquals("FirstName", user.getFirstName());
        assertEquals("LastName", user.getLastName());
        assertEquals(LocalDate.now(), user.getDateJoined());
        assertEquals(0, user.getBorrowed());
    }

    @Test
    public void shouldUpdateLastName() {
        // given
        User user = new User("FirstName", "LastName");

        // when
        user.updateLastName("Name");

        // then
        assertEquals("Name", user.getLastName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenParameterIsNull() {
        // given
        User user = new User("FirstName", "LastName");

        // when
        user.updateLastName(null);
    }

    @Test
    public void borrowShouldIncreaseBorrowed() {
        // given
        User user = new User("FirstName", "LastName");

        // when
        user.borrow();

        // then
        assertEquals(1, user.getBorrowed());
    }

    @Test(expected = BorrowedLimitExceededException.class)
    public void borrowShouldThrowExceptionIfLimitExceeded() {
        // given
        User user = new User("FirstName", "LastName");
        user.borrow();
        user.borrow();
        user.borrow();
        user.borrow();
        user.borrow();

        // when
        user.borrow();
    }

    @Test
    public void returnEditionShouldDecreaseBorrowedIfWasBorrowed() {
        // given
        User user = new User("FirstName", "LastName");
        user.borrow();

        // when
        user.returnEdition();

        // then
        assertEquals(0, user.getBorrowed());
    }

    @Test(expected = InsufficientQuantityToDecreaseException.class)
    public void returnEditionShouldThrowExceptionIfWasNotBorrowed() {
        // given
        User user = new User("FirstName", "LastName");

        // when
        user.returnEdition();
    }

}