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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BookResource resource = (BookResource) o;

        if (id != resource.id) {
            return false;
        }
        if (quantity != resource.quantity) {
            return false;
        }
        if (onLoan != resource.onLoan) {
            return false;
        }
        if (numberOfEditions != resource.numberOfEditions) {
            return false;
        }
        if (title != null ? !title.equals(resource.title) : resource.title != null) {
            return false;
        }
        if (author != null ? !author.equals(resource.author) : resource.author != null) {
            return false;
        }
        return publicationYear != null ? publicationYear.equals(resource.publicationYear) : resource.publicationYear == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (publicationYear != null ? publicationYear.hashCode() : 0);
        result = 31 * result + quantity;
        result = 31 * result + onLoan;
        result = 31 * result + numberOfEditions;
        return result;
    }
}
