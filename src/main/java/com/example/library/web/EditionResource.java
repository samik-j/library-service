package com.example.library.web;

public class EditionResource {

    private long id;
    private String isbn;
    private long quantity;

    public EditionResource() {
    }

    public EditionResource(long id, String isbn, long quantity) {
        this.id = id;
        this.isbn = isbn;
        this.quantity = quantity;
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

}