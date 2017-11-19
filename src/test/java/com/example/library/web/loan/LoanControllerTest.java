package com.example.library.web.loan;

import com.example.library.domain.book.Book;
import com.example.library.domain.edition.Edition;
import com.example.library.domain.loan.Loan;
import com.example.library.domain.loan.LoanService;
import com.example.library.domain.user.User;
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

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LoanControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LoanService service;

    @Mock
    private LoanCreationValidator creationValidator;

    @Mock
    private LoanReturnValidator returnValidator;

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
        LoanController controller = new LoanController(service, creationValidator, returnValidator);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .addFilters(new CorsFilter())
                .build();
    }

    @Test
    public void shouldGetLoansOverdueNowSuccess() throws Exception {
        // given
        Loan loan = createOverdueNotReturnedLoan();
        List<Loan> loans = Arrays.asList(loan);

        when(service.findLoans(LoanOverdue.NOW)).thenReturn(loans);

        // when
        ResultActions result = mockMvc.perform(get("/loans?overdue=NOW"));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is((int) loan.getId())))
                .andExpect(jsonPath("$[0].userId", is((int) loan.getUser().getId())))
                .andExpect(jsonPath("$[0].editionId", is((int) loan.getEdition().getId())))
                //.andExpect(jsonPath("$[0].dateLent", is(loan.getDateLent())))
                //.andExpect(jsonPath("$[0].dateToReturn", is(loan.getDateToReturn)))
                .andExpect(jsonPath("$[0].returned", is(loan.isReturned())))
                .andExpect(jsonPath("$[0].overdue", is(loan.isOverdue())));
    }


    @Test
    public void shouldGetLoansNotReturnedSuccess() throws Exception {
        // given
        Loan loan = createOverdueNotReturnedLoan();
        List<Loan> loans = Arrays.asList(loan);

        boolean returned = false;

        when(service.findLoans(returned)).thenReturn(loans);

        // when
        ResultActions result = mockMvc.perform(get("/loans?returned=false"));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is((int) loan.getId())))
                .andExpect(jsonPath("$[0].userId", is((int) loan.getUser().getId())))
                .andExpect(jsonPath("$[0].editionId", is((int) loan.getEdition().getId())))
                //.andExpect(jsonPath("$[0].dateLent", is(loan.getDateLent())))
                //.andExpect(jsonPath("$[0].dateToReturn", is(loan.getDateToReturn())))
                .andExpect(jsonPath("$[0].returned", is(loan.isReturned())))
                .andExpect(jsonPath("$[0].overdue", is(loan.isOverdue())));
    }

    @Test
    public void shouldGetLoansByBookIdSuccess() throws Exception {
        // given
        Loan loan = createLoan();
        List<Loan> loans = Arrays.asList(loan);

        long bookId = 1;

        when(service.findLoans(bookId)).thenReturn(loans);

        // when
        ResultActions result = mockMvc.perform(get("/loans?bookId=1"));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is((int) loan.getId())))
                .andExpect(jsonPath("$[0].userId", is((int) loan.getUser().getId())))
                .andExpect(jsonPath("$[0].editionId", is((int) loan.getEdition().getId())))
                //.andExpect(jsonPath("$[0].dateLent", is(loan.getDateLent())))
                //.andExpect(jsonPath("$[0].dateToReturn", is(loan.getDateToReturn)))
                .andExpect(jsonPath("$[0].returned", is(loan.isReturned())))
                .andExpect(jsonPath("$[0].overdue", is(loan.isOverdue())));
    }

    @Test
    public void shouldGetLoansAllSuccess() throws Exception {
        // given
        Loan loan = createLoan();
        List<Loan> loans = Arrays.asList(loan);

        when(service.findLoans()).thenReturn(loans);

        // when
        ResultActions result = mockMvc.perform(get("/loans"));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is((int) loan.getId())))
                .andExpect(jsonPath("$[0].userId", is((int) loan.getUser().getId())))
                .andExpect(jsonPath("$[0].editionId", is((int) loan.getEdition().getId())))
                //.andExpect(jsonPath("$[0].dateLent", is(loan.getDateLent())))
                //.andExpect(jsonPath("$[0].dateToReturn", is(loan.getDateToReturn)))
                .andExpect(jsonPath("$[0].returned", is(loan.isReturned())))
                .andExpect(jsonPath("$[0].overdue", is(loan.isOverdue())));
    }

    @Test
    public void shouldLendSuccess() throws Exception {
        // given
        long userId = 0;
        long editionId = 0;
        LoanResource resource = createLoanResource(userId, editionId);
        Loan loan = createLoan();

        when(creationValidator.validate(resource)).thenReturn(new ErrorsResource(new ArrayList<>()));
        when(service.registerLoan(resource)).thenReturn(loan);

        // when
        ResultActions result = mockMvc.perform(post("/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(resource)));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.userId", is((int) userId)))
                .andExpect(jsonPath("$.editionId", is((int) editionId)))
                //.andExpect(jsonPath("$.dateLent", is(LocalDate.now())))
                //.andExpect(jsonPath("$.dateToReturn", is(LocalDate.now().plusDays(14))))
                .andExpect(jsonPath("$.returned", is(false)))
                .andExpect(jsonPath("$.overdue", is(false)));
    }

    @Test
    public void shouldLendFailIfUserDoesNotExist400BadRequest() throws Exception {
        // given
        long userId = 1;
        long editionId = 1;
        LoanResource resource = createLoanResource(userId, editionId);

        when(creationValidator.validate(resource)).thenReturn(new ErrorsResource(Arrays.asList("User does not exist")));

        // when
        ResultActions result = mockMvc.perform(post("/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(resource)));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors", is(Arrays.asList("User does not exist"))));

        verify(creationValidator, times(1)).validate(resource);
        verify(service, times(0)).registerLoan(resource);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldReturnLoanSuccess() throws Exception {
        // given
        long loanId = 1;

        Loan loan = createLoan();
        LoanResource resource = new LoanResource(loan);
        Loan loanReturned = createReturnedLoan(loan);

        when(service.loanExists(loanId)).thenReturn(true);
        when(service.findLoan(loanId)).thenReturn(loan);
        when(returnValidator.validate(resource)).thenReturn(new ErrorsResource(new ArrayList<>()));

        when(service.returnLoan(loanId)).thenReturn(loanReturned);

        // when
        ResultActions result = mockMvc.perform(put("/loans/{loanId}", loanId));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is((int) resource.getId())))
                .andExpect(jsonPath("$.userId", is(resource.getUserId().intValue())))
                .andExpect(jsonPath("$.editionId", is(resource.getEditionId().intValue())))
                //.andExpect(jsonPath("$.dateLent", is(resource.getDateLent())))
                //.andExpect(jsonPath("$.dateToReturn", is(resource.getDateToReturn())))
                .andExpect(jsonPath("$.returned", is(true)))
                .andExpect(jsonPath("$.overdue", is(resource.isOverdue())));
    }

    @Test
    public void shouldReturnLoanFail404NotFound() throws Exception {
        // given
        long loanId = 1;

        when(service.loanExists(loanId)).thenReturn(false);

        // when
        ResultActions result = mockMvc.perform(put("/loans/{loanId}", loanId));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnLoanFailIfLoanAlreadyReturned400BadRequest() throws Exception {
        // given
        long loanId = 1;

        Loan loan = createReturnedLoan(createLoan());
        LoanResource resource = new LoanResource(loan);

        when(service.loanExists(loanId)).thenReturn(true);
        when(service.findLoan(loanId)).thenReturn(loan);
        when(returnValidator.validate(resource)).thenReturn(new ErrorsResource(Arrays.asList("Loan already returned")));

        // when
        ResultActions result = mockMvc.perform(put("/loans/{loanId}", loanId));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.validationErrors", is(Arrays.asList("Loan already returned"))));

        verify(service, times(1)).loanExists(loanId);
        verify(service, times(1)).findLoan(loanId);
        verify(returnValidator, times(1)).validate(resource);
        verify(service, times(0)).returnLoan(loanId);
        verifyNoMoreInteractions(service);
    }

    private Loan createLoan() {
        User user = new User("FirsName", "LastName");
        Book book = new Book("Title", "Author", Year.parse("2000"));
        Edition edition = new Edition("1234567890123", Year.parse("2000"), 5, book);

        return new Loan(user, edition);
    }

    private Loan createOverdueNotReturnedLoan() {
        Loan loan = createLoan();
        Loan loanOverdue = mock(Loan.class);

        when(loanOverdue.getId()).thenReturn(loan.getId());
        when(loanOverdue.getUser()).thenReturn(loan.getUser());
        when(loanOverdue.getEdition()).thenReturn(loan.getEdition());
        when(loanOverdue.getDateLent()).thenReturn(LocalDate.parse("2017-10-10"));
        when(loanOverdue.getDateToReturn()).thenReturn(LocalDate.parse("2017-10-24"));
        when(loanOverdue.isReturned()).thenReturn(false);
        when(loanOverdue.isOverdue()).thenReturn(true);

        return loanOverdue;
    }

    private Loan createReturnedLoan(Loan loan) {
        Loan loanReturned = mock(Loan.class);

        when(loanReturned.getId()).thenReturn(loan.getId());
        when(loanReturned.getUser()).thenReturn(loan.getUser());
        when(loanReturned.getEdition()).thenReturn(loan.getEdition());
        when(loanReturned.getDateLent()).thenReturn(loan.getDateLent());
        when(loanReturned.getDateToReturn()).thenReturn(loan.getDateToReturn());
        when(loanReturned.isReturned()).thenReturn(true);
        when(loanReturned.isOverdue()).thenReturn(loan.isOverdue());

        return loanReturned;
    }

    private LoanResource createLoanResource(long user1Id, long editionId) {
        LoanResource resource = new LoanResource();

        resource.setUserId(user1Id);
        resource.setEditionId(editionId);

        return resource;
    }

}