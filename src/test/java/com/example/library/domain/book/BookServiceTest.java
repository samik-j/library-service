package com.example.library.domain.book;

import com.example.library.web.book.BookResource;
import org.junit.Test;

import java.time.Year;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookServiceTest {

    private BookRepository repository = mock(BookRepository.class);

    private BookService service = new BookService(repository);


    @Test
    public void shouldRegisterBook() {
        // given
        BookResource resource = getResource();

        // when


        // then

    }


    private BookResource getResource() {
        BookResource resource = new BookResource();
        resource.setTitle("title");
        resource.setAuthor("author");
        resource.setPublicationYear(Year.parse("2000"));

        return resource;
    }
}