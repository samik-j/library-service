package com.example.library.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String author;

    private Book() {
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
}
