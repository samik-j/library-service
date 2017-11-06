package com.example.library.web.user;

import com.example.library.domain.user.User;

import java.time.LocalDate;

public class UserResource {

    private long id;
    private String firstName;
    private String lastName;
    private LocalDate dateJoined;
    private int borrowed;

    public UserResource() {
    }

    UserResource(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.dateJoined = user.getDateJoined();
        this.borrowed = user.getBorrowed();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public LocalDate getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(LocalDate dateJoined) {
        this.dateJoined = dateJoined;
    }

    public int getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(int borrowed) {
        this.borrowed = borrowed;
    }
}
