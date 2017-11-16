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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EditionResource resource = (EditionResource) o;

        if (id != resource.id) {
            return false;
        }
        if (quantity != resource.quantity) {
            return false;
        }
        if (onLoan != resource.onLoan) {
            return false;
        }
        if (bookId != resource.bookId) {
            return false;
        }
        if (isbn != null ? !isbn.equals(resource.isbn) : resource.isbn != null) {
            return false;
        }
        return publicationYear != null ? publicationYear.equals(resource.publicationYear) : resource.publicationYear == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (isbn != null ? isbn.hashCode() : 0);
        result = 31 * result + (publicationYear != null ? publicationYear.hashCode() : 0);
        result = 31 * result + (int) (quantity ^ (quantity >>> 32));
        result = 31 * result + (int) (onLoan ^ (onLoan >>> 32));
        result = 31 * result + (int) (bookId ^ (bookId >>> 32));
        return result;
    }
}
