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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserResource resource = (UserResource) o;

        if (id != resource.id) {
            return false;
        }
        if (borrowed != resource.borrowed) {
            return false;
        }
        if (firstName != null ? !firstName.equals(resource.firstName) : resource.firstName != null) {
            return false;
        }
        if (lastName != null ? !lastName.equals(resource.lastName) : resource.lastName != null) {
            return false;
        }
        return dateJoined != null ? dateJoined.equals(resource.dateJoined) : resource.dateJoined == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (dateJoined != null ? dateJoined.hashCode() : 0);
        result = 31 * result + borrowed;
        return result;
    }
}
