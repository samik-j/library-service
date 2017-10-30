package com.example.library.web;

import com.example.library.domain.Edition;

public class EditionResource {

    private long id;
    private String isbn;
    private long quantity;
    private long borrowed;

    public EditionResource() {
    }

    EditionResource(Edition edition) {
        this.id = edition.getId();
        this.isbn = edition.getIsbn();
        this.quantity = edition.getQuantity();
        this.borrowed = edition.getBorrowed();
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

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(long borrowed) {
        this.borrowed = borrowed;
    }
}
