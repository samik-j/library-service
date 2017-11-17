package com.example.library.domain.loan;

import com.example.library.domain.user.User;
import com.example.library.domain.edition.Edition;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToOne
    @JoinColumn(name = "edition_id", nullable = false)
    private Edition edition;
    private LocalDate dateLent;
    private LocalDate dateToReturn;
    private boolean returned;

    private Loan() {
    }

    public Loan(User user, Edition edition) {
        this.user = user;
        this.edition = edition;
        this.dateLent = LocalDate.now();
        this.dateToReturn = dateLent.plusDays(14);
        this.returned = false;
    }


    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Edition getEdition() {
        return edition;
    }

    public LocalDate getDateLent() {
        return dateLent;
    }

    public LocalDate getDateToReturn() {
        return dateToReturn;
    }

    public boolean isReturned() {
        return returned;
    }

    public boolean isOverdue() {
        return dateToReturn.isBefore(LocalDate.now());
    }

    void returnLoan() {
        if(!returned) {
            returned = true;
        }
        else {
            throw new LoanAlreadyReturnedException(id);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Loan loan = (Loan) o;

        return id == loan.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
