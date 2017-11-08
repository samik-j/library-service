package com.example.library.domain.user;

import com.example.library.domain.InsufficientQuantityToDecreaseException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String firstName;
    private String lastName;
    private LocalDate dateJoined;
    private int borrowed;

    User() {
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateJoined = LocalDate.now();
        this.borrowed = 0;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateJoined() {
        return dateJoined;
    }

    public int getBorrowed() {
        return borrowed;
    }

    void updateLastName(String updatedName) {
        if (updatedName == null) {
            throw new IllegalArgumentException();
        }
        this.lastName = updatedName;
    }

    public void borrow() {
        if (canBorrow()) {
            ++borrowed;
        } else {
            throw new BorrowedLimitExceededException(id);
        }
    }

    boolean canBorrow() {
        return borrowed < 5;
    }

    public void returnEdition() {
        if (borrowed > 0) {
            --borrowed;
        } else {
            throw new InsufficientQuantityToDecreaseException();
        }
    }

    boolean canReturn() {
        return borrowed > 0;
    }

}
