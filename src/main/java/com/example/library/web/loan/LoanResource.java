package com.example.library.web.loan;

import com.example.library.domain.loan.Loan;

import java.time.LocalDate;

public class LoanResource {

    private long id;
    private long userId;
    private long editionId;
    private LocalDate dateLent;
    private LocalDate dateToReturn;
    private boolean isOverdue;
    private boolean returned;

    public LoanResource() {
    }

    public LoanResource(Loan loan) {
        this.id = loan.getId();
        this.userId = loan.getUser().getId();
        this.editionId = loan.getEdition().getId();
        this.dateLent = loan.getDateLent();
        this.dateToReturn = loan.getDateToReturn();
        this.isOverdue = loan.isOverdue();
        this.returned = loan.isReturned();
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

    public LocalDate getDateLent() {
        return dateLent;
    }

    public void setDateLent(LocalDate dateLent) {
        this.dateLent = dateLent;
    }

    public LocalDate getDateToReturn() {
        return dateToReturn;
    }

    public void setDateToReturn(LocalDate dateToReturn) {
        this.dateToReturn = dateToReturn;
    }

    public boolean isOverdue() {
        return isOverdue;
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }
}