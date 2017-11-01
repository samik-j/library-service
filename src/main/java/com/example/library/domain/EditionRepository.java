package com.example.library.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface EditionRepository extends JpaRepository<Edition, Long> { // <Edition, Long> <typ Entity, typ id ktore ma Entity> {

    @Query("SELECT edition FROM Edition edition WHERE edition.book.id = :bookId AND (:isbn IS NULL OR edition.isbn = :isbn)")
    Set<Edition> findEditions(@Param("bookId") long bookId, @Param("isbn") String isbn);

}
