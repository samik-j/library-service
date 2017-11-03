package com.example.library.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.Year;
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
    @OneToMany(fetch = FetchType.LAZY)
    // lazy wczyta mi edycje dopiero jak bede ich uzywac albo moze byc EAGER to wtedy jak wczytuje Book to odrazu wczyta z calym setem Editin
    private Set<Edition> editions;

    Book() {
    }

    Book(String title, String author, Year publicationYear) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
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

    public void setTitle(String title) {
        this.title = title;
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

    public void setEditions(Set<Edition> editions) {
        this.editions = editions;
    }

    void updateTitle(String title) {
        if(title == null) {
            throw new IllegalArgumentException();
        }
        this.title = title;
    }

    void updateAuthor(String author) { // brak modyfikatora znaczu ze jest package private
        if(author == null) {
            throw new IllegalArgumentException();
        }
        this.author = author;
    }

    void addEdition(Edition edition) {
        editions.add(edition);
    }

}
