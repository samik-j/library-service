package com.example.library.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id") // DODAC NULLABLE FALSE I W BAZIE DANYCH ZMIENIC
    private User user;
    @OneToOne
    @JoinColumn(name = "edition_id") // DODAC NULLABLE FALSE I W BAZIE DANYCH ZMIENIC
    private Edition edition;
    private String dateLent;
    private LocalDate dateToReturn;

    Loan() {
    }

    Loan(User user, Edition edition) {
        this.user = user;
        this.edition = edition;
        this.dateLent = LocalDate.now().toString();
        this.dateToReturn = LocalDate.now().plusDays(14);
    }


    public long getId() {
        return id;
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

    public String getDateLent() {
        return dateLent;
    }

    public void setDateLent(String dateLent) {
        this.dateLent = dateLent;
    }

    public LocalDate getDateToReturn() {
        return dateToReturn;
    }

    public void setDateToReturn(LocalDate dateToReturn) {
        this.dateToReturn = dateToReturn;
    }

    public boolean isOverdue() {
        return dateToReturn.isBefore(LocalDate.now());
    }

}
