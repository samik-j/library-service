package com.example.library.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EditionRepository extends JpaRepository<Edition, Long> { // <Edition, Long> <typ Entity, typ id ktore ma Entity> {

    List<Edition> findByIsbn(String isbn);
}
