package com.example.library.domain;

import javax.persistence.*;

@Entity
public class Edition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String isbn;
    private long quantity;
    private long onLoan;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    Edition() {
    }

    Edition(String isbn, long quantity, Book book) {
        this.isbn = isbn;
        this.quantity = quantity;
        this.book = book;
        this.onLoan = 0;
    }

    public long getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book_id) {
        this.book= book_id;
    }

    public long getOnLoan() {
        return onLoan;
    }

    public void setOnLoan(long onLoan) {
        this.onLoan = onLoan;
    }

    boolean lend() {
        if(quantity > onLoan) {
            ++onLoan;
            return true;
        }
        return false;
    }
}
