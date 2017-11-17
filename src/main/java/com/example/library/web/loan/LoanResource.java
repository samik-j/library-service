package com.example.library.web.loan;

import com.example.library.domain.loan.Loan;

import java.time.LocalDate;

public class LoanResource {

    private long id;
    private Long userId;
    private Long editionId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEditionId() {
        return editionId;
    }

    public void setEditionId(Long editionId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LoanResource resource = (LoanResource) o;

        if (id != resource.id) {
            return false;
        }
        if (isOverdue != resource.isOverdue) {
            return false;
        }
        if (returned != resource.returned) {
            return false;
        }
        if (userId != null ? !userId.equals(resource.userId) : resource.userId != null) {
            return false;
        }
        if (editionId != null ? !editionId.equals(resource.editionId) : resource.editionId != null) {
            return false;
        }
        if (dateLent != null ? !dateLent.equals(resource.dateLent) : resource.dateLent != null) {
            return false;
        }
        return dateToReturn != null ? dateToReturn.equals(resource.dateToReturn) : resource.dateToReturn == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (editionId != null ? editionId.hashCode() : 0);
        result = 31 * result + (dateLent != null ? dateLent.hashCode() : 0);
        result = 31 * result + (dateToReturn != null ? dateToReturn.hashCode() : 0);
        result = 31 * result + (isOverdue ? 1 : 0);
        result = 31 * result + (returned ? 1 : 0);
        return result;
    }
}
