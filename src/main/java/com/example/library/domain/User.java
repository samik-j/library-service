package com.example.library.domain;

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
    private String dateJoined;

    private User() {
    }

    User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateJoined = LocalDate.now().toString();
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(String dateJoined) {
        this.dateJoined = dateJoined;
    }

    void updateLastName(String updatedName) {
        if (updatedName == null) {
            throw new IllegalArgumentException();
        }
        this.lastName = updatedName;
    }
}
