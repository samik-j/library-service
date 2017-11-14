package com.example.library.domain.book;

import com.example.library.domain.edition.Edition;

import javax.persistence.*;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String title;
    private String author;
    private Year publicationYear;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    // lazy wczyta mi edycje dopiero jak bede ich uzywac albo moze byc EAGER to wtedy jak wczytuje Book to odrazu wczyta z calym setem Editin
    private Set<Edition> editions;

    Book() {
    }

    public Book(String title, String author, Year publicationYear) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.editions = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Set<Edition> getEditions() {
        return editions;
    }

    public Year getPublicationYear() {
        return publicationYear;
    }

    void updateTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException();
        }
        this.title = title;
    }

    void updateAuthor(String author) {
        if (author == null) {
            throw new IllegalArgumentException();
        }
        this.author = author;
    }

    public void addEdition(Edition edition) {
        editions.add(edition);
    }

    public int getQuantity() {
        int quantity = 0;

        for(Edition edition : editions) {
            quantity += edition.getQuantity();
        }

        return quantity;
    }

    public int getOnLoanQuantity() {
        int onLoan = 0;

        for(Edition edition : editions) {
            onLoan += edition.getOnLoan();
        }

        return onLoan;
    }

    public int getNumberOfEditions() {
        return editions.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Book book = (Book) o;

        if (id != book.id) {
            return false;
        }
        if (title != null ? !title.equals(book.title) : book.title != null) {
            return false;
        }
        if (author != null ? !author.equals(book.author) : book.author != null) {
            return false;
        }
        if (publicationYear != null ? !publicationYear.equals(book.publicationYear) : book.publicationYear != null) {
            return false;
        }
        return editions != null ? editions.equals(book.editions) : book.editions == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (publicationYear != null ? publicationYear.hashCode() : 0);
        result = 31 * result + (editions != null ? editions.hashCode() : 0);
        return result;
    }
}
