package com.example.library.web;

import com.example.library.domain.Borrowed;

public class BorrowedResource {

    private long id;
    private long userId;
    private long editionId;
    private String dateBorrowed;
    private String dateToReturn;
    private boolean isOverdue;

    public BorrowedResource() {
    }

    public BorrowedResource(Borrowed borrowed) {
        this.id = borrowed.getId();
        this.userId = borrowed.getUser().getId();
        this.editionId = borrowed.getEdition().getId();
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getEditionId() {
        return editionId;
    }

    public void setEditionId(long editionId) {
        this.editionId = editionId;
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
