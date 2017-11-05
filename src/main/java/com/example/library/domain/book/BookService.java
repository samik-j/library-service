package com.example.library.domain.book;

import com.example.library.web.book.BookResource;
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

    public Book registerBook(BookResource resource) {
        Book book = new Book(resource.getTitle(), resource.getAuthor(), resource.getPublicationYear());

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

    public List<Book> findBooks(String title, String author) {
        return repository.findBooks(title, author);
    }

    public boolean hasNoSuchBook(BookResource resource) {
        return repository.findBook(resource.getTitle(), resource.getAuthor()) == null;
    }
}
