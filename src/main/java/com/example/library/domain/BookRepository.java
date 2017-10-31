package com.example.library.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>{ // <Book, Long> <typ Entity, typ id ktore ma Entity>

    @Query("select book from Book book where " +
            "(:title is null or book.title like concat ('%', :title, '%')) and " +
            "(:author is null or book.author like concat ('%', :author, '%'))")
    List<Book> findBooks(@Param("title") String title, @Param("author") String author);

}
