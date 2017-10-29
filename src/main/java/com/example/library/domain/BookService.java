package com.example.library.domain;

import com.example.library.web.BookResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookService {

    private BookRepository repository;

    @Autowired
    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Book registerBook(BookResource resource) {
        Book book = new Book(resource.getTitle(), resource.getAuthor());

        return repository.save(book);
    }

    public Book updateBook(Long id, BookResource resource) {
        Book book = repository.findOne(id);
        book.updateAuthor(resource.getAuthor());
        book.updateTitle(resource.getTitle());

        return repository.save(book);
    }

    public Book findBookById(long id) {
        return repository.findOne(id);
    }

    public List<Book> findByTitleAndAuthor(String title, String author) {
        if(title != null && author != null) {
            return repository.findByTitleContainingAndAuthorContaining(title, author);
        }
        else if(author != null) {
            return repository.findByAuthorContaining(author);
        }
        else if(title != null){
            return repository.findByTitleContaining(title);
        }
        else {
            return repository.findAll();
        }
    }
}
