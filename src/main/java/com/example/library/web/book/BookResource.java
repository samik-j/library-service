package com.example.library.web.book;

import com.example.library.domain.book.Book;

import java.time.Year;

public class BookResource {

    private long id;
    private String title;
    private String author;
    private Year publicationYear;
    private int quantity;
    private int onLoan;
    private int numberOfEditions;

    public BookResource() {
    }

    BookResource(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publicationYear = book.getPublicationYear();
        this.quantity = book.getQuantity();
        this.onLoan = book.getOnLoanQuantity();
        this.numberOfEditions = book.getNumberOfEditions();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Year getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Year publicationYear) {
        this.publicationYear = publicationYear;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOnLoan() {
        return onLoan;
    }

    public void setOnLoan(int onLoan) {
        this.onLoan = onLoan;
    }

    public int getNumberOfEditions() {
        return numberOfEditions;
    }

    public void setNumberOfEditions(int numberOfEditions) {
        this.numberOfEditions = numberOfEditions;
    }
}
