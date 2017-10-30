package com.example.library.web;

import com.example.library.domain.Borrowed;
import com.example.library.domain.Edition;
import com.example.library.domain.User;

public class BorrowedResource {

    private long id;
    private User user;
    private Edition edition;
    private String dateBorrowed;
    private String dateToReturn;
    private boolean isOverdue;

    public BorrowedResource() {
    }

    public BorrowedResource(Borrowed borrowed) {
        this.id = borrowed.getId();
        this.user = borrowed.getUser();
        this.edition = borrowed.getEdition();
        this.dateBorrowed = borrowed.getDateBorrowed();
        this.dateToReturn = borrowed.getDateToReturn();
        this.isOverdue = borrowed.isOverdue();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Edition getEdition() {
        return edition;
    }

    public void setEdition(Edition edition) {
        this.edition = edition;
    }

    public String getDateBorrowed() {
        return dateBorrowed;
    }

    public void setDateBorrowed(String dateBorrowed) {
        this.dateBorrowed = dateBorrowed;
    }

    public String getDateToReturn() {
        return dateToReturn;
    }

    public void setDateToReturn(String dateToReturn) {
        this.dateToReturn = dateToReturn;
    }

    public boolean isOverdue() {
        return isOverdue;
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }
}
