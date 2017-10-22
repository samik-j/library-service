package com.example.library.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long>{ // <Book, Long> <typ Entity, typ id ktore ma Entity>
}
