package com.example.library.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

@Entity
public class Edition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String isbn;
    private long quantity;
    private long borrowed;
    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonManagedReference
    private Book book;

    Edition() {
    }

    Edition(String isbn, long quantity, Book book) {
        this.isbn = isbn;
        this.quantity = quantity;
        this.book = book;
        this.borrowed = 0;
    }

    Edition(String isbn, long quantity, long borrowed, Book book) {
        this.isbn = isbn;
        this.quantity = quantity;
        this.borrowed = borrowed;
        this.book = book;
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

    public long getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(long borrowed) {
        this.borrowed = borrowed;
    }

    boolean borrow() {
        if(quantity > borrowed) {
            ++borrowed;
            return true;
        }
        return false;
    }
}
