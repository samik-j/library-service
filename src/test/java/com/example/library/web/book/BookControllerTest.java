package com.example.library.web.book;

import com.example.library.domain.book.Book;
import com.example.library.domain.book.BookService;
import com.example.library.web.ErrorsResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.catalina.filters.CorsFilter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService service;

    @Mock
    private BookCreationValidator creationValidator;

    @Mock
    private BookUpdateValidator updateValidator;

    private static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        BookController controller = new BookController(service, creationValidator, updateValidator);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .addFilters(new CorsFilter())
                .build();

    }

    @Test
    public void shouldGetAllBooksSuccess() throws Exception {
        // given
        List<Book> books = new ArrayList<>();
        books.add(new Book("title", "author", Year.parse("2000")));
        books.add(new Book("title2", "author2", Year.parse("2000")));

        String title = null;
        String author = null;

        when(service.findBooks(title, author)).thenReturn(books);

        // when
        ResultActions result = mockMvc.perform(get("/books"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("title")))
                .andExpect(jsonPath("$[0].author", is("author")))
                .andExpect(jsonPath("$[0].publicationYear", is(2000)))
                .andExpect(jsonPath("$[0].quantity", is(0)))
                .andExpect(jsonPath("$[0].onLoan", is(0)))
                .andExpect(jsonPath("$[0].numberOfEditions", is(0)))
                .andExpect(jsonPath("$[1].title", is("title2")))
                .andExpect(jsonPath("$[1].author", is("author2")))
                .andExpect(jsonPath("$[1].publicationYear", is(2000)))
                .andExpect(jsonPath("$[1].quantity", is(0)))
                .andExpect(jsonPath("$[1].onLoan", is(0)))
                .andExpect(jsonPath("$[1].numberOfEditions", is(0)));
    }

    @Test
    public void shouldGetBooksByTitleSuccess() throws Exception {
        // given
        List<Book> books = new ArrayList<>();
        books.add(new Book("title2", "author2", Year.parse("2000")));

        String title = "2";
        String author = null;

        when(service.findBooks(title, author)).thenReturn(books);

        // when
        ResultActions result = mockMvc.perform(get("/books?title=2"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("title2")))
                .andExpect(jsonPath("$[0].author", is("author2")))
                .andExpect(jsonPath("$[0].publicationYear", is(2000)))
                .andExpect(jsonPath("$[0].quantity", is(0)))
                .andExpect(jsonPath("$[0].onLoan", is(0)))
                .andExpect(jsonPath("$[0].numberOfEditions", is(0)));
    }

    @Test
    public void shouldGetBooksByAuthorSuccess() throws Exception {
        // given
        List<Book> books = new ArrayList<>();

        String title = null;
        String author = "author";

        when(service.findBooks(title, author)).thenReturn(books);

        // when
        ResultActions result = mockMvc.perform(get("/books?author=author"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldGetBookByIdSuccess() throws Exception {
        // given
        Book book = new Book("title2", "author2", Year.parse("2000"));

        when(service.findBookById(10)).thenReturn(book);
        //given(service.findBookById(10)).willReturn(book);

        // when
        ResultActions result = mockMvc.perform(get("/books/{bookId}", 10));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.title", is("title2")))
                .andExpect(jsonPath("$.author", is("author2")))
                .andExpect(jsonPath("$.publicationYear", is(2000)))
                .andExpect(jsonPath("$.quantity", is(0)))
                .andExpect(jsonPath("$.onLoan", is(0)))
                .andExpect(jsonPath("$.numberOfEditions", is(0)));
    }

    @Test
    public void shouldGetBookByIdFail404NotFound() throws Exception {
        // given
        when(service.findBookById(0)).thenReturn(null);

        // when
        ResultActions result = mockMvc.perform(get("/books/{bookId}", 0));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void shouldRegisterBookSuccess() throws Exception {
        // given
        BookResource resource = createBookResource("title", "author", Year.parse("2000"));
        Book book = new Book("title", "author", Year.parse("2000"));

        when(creationValidator.validate(resource)).thenReturn(new ErrorsResource(new ArrayList<>()));
        when(service.registerBook(resource)).thenReturn(book);

        // when
        ResultActions result = mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(resource)));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.title", is("title")))
                .andExpect(jsonPath("$.author", is("author")))
                .andExpect(jsonPath("$.publicationYear", is(2000)))
                .andExpect(jsonPath("$.quantity", is(0)))
                .andExpect(jsonPath("$.onLoan", is(0)))
                .andExpect(jsonPath("$.numberOfEditions", is(0)));
    }

    @Test
    public void shouldRegisterBookFailIfBookAlreadyExists400BadRequest() throws Exception {
        // given
        BookResource resource = createBookResource("title2", "author2", Year.parse("2000"));

        when(creationValidator.validate(resource)).thenReturn(new ErrorsResource(Arrays.asList("Book already exists")));

        // when
        ResultActions result = mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(resource)));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.validationErrors", is(Arrays.asList("Book already exists"))));

        verify(creationValidator, times(1)).validate(resource);
        verify(service, times(0)).registerBook(resource);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldUpdateBookSuccess() throws Exception {
        // given
        BookResource resource = createBookResource("title2", "author2");
        Book bookUpdated = new Book("title2", "author2", Year.parse("2000"));
        long bookId = 0;

        when(service.bookExists(bookId)).thenReturn(true);
        when(updateValidator.validate(resource)).thenReturn(new ErrorsResource(new ArrayList<>()));
        when(service.updateBook(bookId, resource)).thenReturn(bookUpdated);

        // when
        ResultActions result = mockMvc.perform(put("/books/{bookId}", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(resource)));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.title", is("title2")))
                .andExpect(jsonPath("$.author", is("author2")))
                .andExpect(jsonPath("$.publicationYear", is(2000)))
                .andExpect(jsonPath("$.quantity", is(0)))
                .andExpect(jsonPath("$.onLoan", is(0)))
                .andExpect(jsonPath("$.numberOfEditions", is(0)));
    }

    @Test
    public void shouldUpdateBookFail404NotFound() throws Exception {
        // given
        BookResource resource = createBookResource("title2", "author2");
        long bookId = 1;

        when(service.bookExists(bookId)).thenReturn(false);

        //when
        ResultActions result = mockMvc.perform(put("/books/{bookId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(resource)));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateBookFailIfParameterIsNull400BadRequest() throws Exception {
        // given
        BookResource resource = createBookResource(null, "author2");
        long bookId = 1;

        when(service.bookExists(bookId)).thenReturn(true);
        when(updateValidator.validate(resource)).thenReturn(new ErrorsResource(Arrays.asList("Title not specified")));

        // when
        ResultActions result = mockMvc.perform(put("/books/{bookId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(resource)));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.validationErrors", is(Arrays.asList("Title not specified"))));

        verify(updateValidator, times(1)).validate(resource);
        verify(service, times(1)).bookExists(bookId);
        verify(service, times(0)).updateBook(bookId, resource);
        verifyNoMoreInteractions(service);
    }

    private BookResource createBookResource(String title, String author) {
        BookResource resource = new BookResource();

        resource.setTitle(title);
        resource.setAuthor(author);

        return resource;
    }

    private BookResource createBookResource(String title, String author, Year publicationYear) {
        BookResource resource = new BookResource();

        resource.setTitle(title);
        resource.setAuthor(author);
        resource.setPublicationYear(publicationYear);

        return resource;
    }

}
