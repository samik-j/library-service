package com.example.library.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface BookRepository extends JpaRepository<Book, Long>{ // <Book, Long> <typ Entity, typ id ktore ma Entity>

    Set<Book> findByTitleContaining(String title);
    Set<Book> findByAuthorContaining(String author);
    Set<Book> findByTitleContainingAndAuthorContaining(String title, String author);

}
