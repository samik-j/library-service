package com.example.library.domain.edition;

import com.example.library.domain.book.Book;

import javax.persistence.*;
import java.time.Year;

@Entity
public class Edition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    //length zeby bylo?
    private String isbn;
    private Year publicationYear;
    private long quantity;
    private long onLoan;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    Edition() {
    }

    public Edition(String isbn, Year publicationYear, long quantity, Book book) {
        this.isbn = isbn;
        this.publicationYear = publicationYear;
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

    public Year getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Year publicationYear) {
        this.publicationYear = publicationYear;
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
        this.book = book_id;
    }

    public long getOnLoan() {
        return onLoan;
    }

    public void setOnLoan(long onLoan) {
        this.onLoan = onLoan;
    }

    public void lend() {
        if (canBeLend()) {
            ++onLoan;
        } else {
            throw new InsufficientEditionQuantityException(id);
        }
    }

    public void returnEdition() {
        --onLoan;
    }

    boolean canBeLend() {
        return quantity > onLoan;
    }
}
