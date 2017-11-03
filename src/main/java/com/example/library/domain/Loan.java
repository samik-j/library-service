package com.example.library.domain;

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

    private Loan() {
    }

    Loan(User user, Edition edition) {
        this.user = user;
        this.edition = edition;
        this.dateLent = LocalDate.now();
        this.dateToReturn = dateLent.plusDays(14);
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

    public boolean isOverdue() {
        return dateToReturn.isBefore(LocalDate.now());
    }

}
