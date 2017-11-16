package com.example.library.web.edition;


import com.example.library.domain.book.Book;
import com.example.library.domain.book.BookService;
import com.example.library.domain.edition.Edition;
import com.example.library.domain.edition.EditionService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EditionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EditionService editionService;

    @Mock
    private BookService bookService;

    @Mock
    private EditionCreationValidator validator;

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
        EditionController controller = new EditionController(editionService, bookService, validator);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .addFilters(new CorsFilter())
                .build();
    }

    private EditionResource getResource() {
        EditionResource resource = new EditionResource();
        resource.setIsbn("1234567890123");
        resource.setPublicationYear(Year.parse("2000"));
        resource.setQuantity(5);

        return resource;
    }

    @Test
    public void shouldGetAllEditionsSuccess() throws Exception {
        // given
        Book book = mock(Book.class);
        long bookId = 1;

        List<Edition> editions = new ArrayList<>();
        editions.add(new Edition("1234567890123", Year.parse("2001"), 5, book));
        editions.add(new Edition("1234567890111", Year.parse("2010"), 3, book));

        String isbn = null;

        when(book.getId()).thenReturn(bookId);
        when(bookService.bookExists(bookId)).thenReturn(true);
        when(editionService.findEditions(bookId, isbn)).thenReturn(editions);

        // when
        ResultActions result = mockMvc.perform(get("/books/{bookId}/editions", bookId));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].isbn", is("1234567890123")))
                .andExpect(jsonPath("$[0].publicationYear", is(2001)))
                .andExpect(jsonPath("$[0].quantity", is(5)))
                .andExpect(jsonPath("$[0].onLoan", is(0)))
                .andExpect(jsonPath("$[0].bookId", is(1)))
                .andExpect(jsonPath("$[1].isbn", is("1234567890111")))
                .andExpect(jsonPath("$[1].publicationYear", is(2010)))
                .andExpect(jsonPath("$[1].quantity", is(3)))
                .andExpect(jsonPath("$[1].onLoan", is(0)))
                .andExpect(jsonPath("$[1].bookId", is(1)));
    }

    @Test
    public void shouldGetEditionsByIsbnSuccess() throws Exception {
        // given
        Book book = mock(Book.class);
        long bookId = 1;

        List<Edition> editions = new ArrayList<>();
        editions.add(new Edition("1234567890123", Year.parse("2001"), 5, book));

        String isbn = "1234567890123";

        when(book.getId()).thenReturn(bookId);
        when(bookService.bookExists(bookId)).thenReturn(true);
        when(editionService.findEditions(bookId, isbn)).thenReturn(editions);

        // when
        ResultActions result = mockMvc.perform(get("/books/{bookId}/editions?isbn=1234567890123", bookId));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].isbn", is("1234567890123")))
                .andExpect(jsonPath("$[0].publicationYear", is(2001)))
                .andExpect(jsonPath("$[0].quantity", is(5)))
                .andExpect(jsonPath("$[0].onLoan", is(0)))
                .andExpect(jsonPath("$[0].bookId", is(1)));
    }

    @Test
    public void shouldGetEditionsFail404NotFound() throws Exception {
        // given
        long bookId = 1;

        when(bookService.bookExists(bookId)).thenReturn(false);

        // when
        ResultActions result = mockMvc.perform(get("/books/{bookId}/editions", bookId));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void shouldAddEditionSuccess() throws Exception {
        // given
        Book book = mock(Book.class);
        long bookId = 1;
        Edition edition = new Edition("1234567890123", Year.parse("2000"), 5, book);
        EditionResource resource = getResource();

        when(book.getId()).thenReturn(bookId);
        when(bookService.bookExists(bookId)).thenReturn(true);
        when(validator.validate(resource)).thenReturn(new ErrorsResource(new ArrayList<>()));
        when(editionService.registerEdition(bookId, resource)).thenReturn(edition);

        // when
        ResultActions result = mockMvc.perform(post("/books/{bookId}/editions", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(resource)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn", is("1234567890123")))
                .andExpect(jsonPath("$.publicationYear", is(2000)))
                .andExpect(jsonPath("$.quantity", is(5)))
                .andExpect(jsonPath("$.onLoan", is(0)))
                .andExpect(jsonPath("$.bookId", is(1)));
    }

    @Test
    public void shouldAddEditionFail404NotFound() throws Exception {
        // given
        long bookId = 1;
        EditionResource resource = getResource();

        when(bookService.bookExists(bookId)).thenReturn(false);

        // when
        ResultActions result = mockMvc.perform(post("/books/{bookId}/editions", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(resource)));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void shouldAddEditionFail400BadRequest() throws Exception {
        // given
        long bookId = 1;
        EditionResource resource = getResource();

        when(bookService.bookExists(bookId)).thenReturn(true);
        when(validator.validate(resource)).thenReturn(new ErrorsResource(Arrays.asList("Isbn already exists")));

        // when
        ResultActions result = mockMvc.perform(post("/books/{bookId}/editions", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(resource)));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.validationErrors", is(Arrays.asList("Isbn already exists"))));

        verify(bookService, times(1)).bookExists(bookId);
        verify(validator, times(1)).validate(resource);
        verify(editionService, times(0)).registerEdition(bookId, resource);
        verifyNoMoreInteractions(editionService);
    }

}