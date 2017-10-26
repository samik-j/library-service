package com.example.library.domain;

import com.example.library.web.BookResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private BookRepository repository;

    @Autowired
    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll() {
        return this.repository.findAll();
    }

    public Book registerBook(BookResource resource) {
        Book book = new Book(resource.getTitle(), resource.getAuthor());
        return repository.save(book);
    }

    public Book updateBook(Long id, BookResource resource) {
        Book book = this.repository.findOne(id);
        book.updateAuthor(resource.getAuthor());
        book.updateTitle(resource.getTitle());
        return repository.save(book);
    }

    public void registerEdition(long id, Edition edition) {
        Book book = this.repository.findOne(id);
        book.addEdition(edition);
        this.repository.save(book);
    }

    public Book findBookById(long id) {
        return this.repository.findOne(id);
    }
}
