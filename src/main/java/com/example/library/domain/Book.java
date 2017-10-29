package com.example.library.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String author;
    @OneToMany(mappedBy = "book")
    private Set<Edition> editions;

    public Book() {
    }

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
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
