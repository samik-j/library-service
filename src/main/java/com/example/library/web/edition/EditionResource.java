package com.example.library.web.edition;

import com.example.library.domain.edition.Edition;

import java.time.Year;

public class EditionResource {
    private long id;
    private String isbn;
    private Year publicationYear;
    private long quantity;
    private long onLoan;
    private long bookId;

    public EditionResource() {
    }

    EditionResource(Edition edition) {
        this.id = edition.getId();
        this.isbn = edition.getIsbn();
        this.publicationYear = edition.getPublicationYear();
        this.quantity = edition.getQuantity();
        this.onLoan = edition.getOnLoan();
        this.bookId = edition.getBook().getId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getOnLoan() {
        return onLoan;
    }

    public void setOnLoan(long onLoan) {
        this.onLoan = onLoan;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }
}
