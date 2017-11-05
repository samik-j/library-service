package com.example.library.domain.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>{ // <Book, Long> <typ Entity, typ id ktore ma Entity>

    @Query("SELECT book FROM Book book WHERE " +
            "(:title IS NULL OR book.title LIKE CONCAT ('%', :title, '%')) AND " +
            "(:author IS NULL OR book.author LIKE CONCAT ('%', :author, '%')) " +
            "ORDER BY book.title")
    List<Book> findBooks(@Param("title") String title, @Param("author") String author);

    @Query("SELECT book FROM Book book WHERE " +
            "book.title LIKE :title AND book.author LIKE :author")
    Book findBook(@Param("title") String title, @Param("author") String author);

    //existsByTitleAndAuthor
}
