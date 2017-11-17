package com.example.library.domain.edition;

import com.example.library.domain.InsufficientQuantityToDecreaseException;
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

    public Year getPublicationYear() {
        return publicationYear;
    }

    public long getQuantity() {
        return quantity;
    }

    public Book getBook() {
        return book;
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

    boolean canBeLend() {
        return quantity > onLoan;
    }

    public void returnEdition() {
        if (onLoan > 0) {
            --onLoan;
        } else {
            throw new InsufficientQuantityToDecreaseException();
        }
    }

    boolean canBeReturned() {
        return onLoan > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Edition edition = (Edition) o;

        return id == edition.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
