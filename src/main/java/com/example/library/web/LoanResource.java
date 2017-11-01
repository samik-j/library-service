package com.example.library.web;

import com.example.library.domain.Loan;

public class LoanResource {

    private long id;
    private long userId;
    private long editionId;
    private String dateLent;
    private String dateToReturn;
    private boolean isOverdue;

    public LoanResource() {
    }

    public LoanResource(Loan loan) {
        this.id = loan.getId();
        this.userId = loan.getUser().getId();
        this.editionId = loan.getEdition().getId();
        this.dateLent = loan.getDateLent();
        this.dateToReturn = loan.getDateToReturn();
        this.isOverdue = loan.isOverdue();
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

    public String getDateLent() {
        return dateLent;
    }

    public void setDateLent(String dateLent) {
        this.dateLent = dateLent;
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
