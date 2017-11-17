package com.example.library.web.loan;

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

    private Loan getNewLoan(long user1Id, long editionId) {
        User user = mock(User.class);
        Edition edition = mock(Edition.class);
        Loan loan = new Loan(user, edition);

        when(user.getId()).thenReturn(user1Id);
        when(edition.getId()).thenReturn(editionId);

        return loan;
    }

    private Loan getLoanOverdueNotReturnedMock(long loanId, long userId, long editionId) {
        Loan loan = mock(Loan.class);
        User user = mock(User.class);
        Edition edition = mock(Edition.class);

        when(user.getId()).thenReturn(userId);
        when(edition.getId()).thenReturn(editionId);
        when(loan.getId()).thenReturn(loanId);
        when(loan.getUser()).thenReturn(user);
        when(loan.getEdition()).thenReturn(edition);
        when(loan.getDateLent()).thenReturn(LocalDate.parse("2017-10-10"));
        when(loan.getDateToReturn()).thenReturn(LocalDate.parse("2017-10-24"));
        when(loan.isReturned()).thenReturn(false);
        when(loan.isOverdue()).thenReturn(true);

        return loan;
    }

    private Loan getNewLoanReturnedMock(long loanId, long userId, long editionId) {
        Loan loan = mock(Loan.class);
        User user = mock(User.class);
        Edition edition = mock(Edition.class);

        when(user.getId()).thenReturn(userId);
        when(edition.getId()).thenReturn(editionId);
        when(loan.getId()).thenReturn(loanId);
        when(loan.getUser()).thenReturn(user);
        when(loan.getEdition()).thenReturn(edition);
        when(loan.getDateLent()).thenReturn(LocalDate.now());
        when(loan.getDateToReturn()).thenReturn(LocalDate.now().plusDays(14));
        when(loan.isReturned()).thenReturn(true);
        when(loan.isOverdue()).thenReturn(false);

        return loan;
    }

    private LoanResource getLoanResource(long user1Id, long editionId) {
        LoanResource resource = new LoanResource();

        resource.setUserId(user1Id);
        resource.setEditionId(editionId);

        return resource;
    }

    @Test
    public void shouldGetLoansOverdueNowSuccess() throws Exception {
        // given
        long loanId = 1;
        long userId = 1;
        long editionId = 1;
        Loan loan = getLoanOverdueNotReturnedMock(loanId, userId, editionId);
        List<Loan> loans = Arrays.asList(loan);

        when(service.findLoans(LoanOverdue.NOW)).thenReturn(loans);

        // when
        ResultActions result = mockMvc.perform(get("/loans?overdue=NOW"));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].editionId", is(1)))
                //.andExpect(jsonPath("$[0].dateLent", is(LocalDate.parse("2017-10-10"))))
                //.andExpect(jsonPath("$[0].dateToReturn", is(LocalDate.parse("2017-10-24"))))
                .andExpect(jsonPath("$[0].returned", is(false)))
                .andExpect(jsonPath("$[0].overdue", is(true)));
    }


    @Test
    public void shouldGetLoansNotReturnedSuccess() throws Exception {
        // given
        long userId = 1;
        long editionId = 1;
        long loanId = 1;
        Loan loan = getLoanOverdueNotReturnedMock(loanId, userId, editionId);
        List<Loan> loans = Arrays.asList(loan);

        boolean returned = false;

        when(service.findLoans(returned)).thenReturn(loans);

        // when
        ResultActions result = mockMvc.perform(get("/loans?returned=false"));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].editionId", is(1)))
                //.andExpect(jsonPath("$[0].dateLent", is(LocalDate.parse("2017-10-10"))))
                //.andExpect(jsonPath("$[0].dateToReturn", is(LocalDate.parse("2017-10-24"))))
                .andExpect(jsonPath("$[0].returned", is(false)))
                .andExpect(jsonPath("$[0].overdue", is(true)));
    }

    @Test
    public void shouldGetLoansByBookIdSuccess() throws Exception {
        // given
        long userId = 1;
        long editionId = 1;
        Loan loan = getNewLoan(userId, editionId);
        List<Loan> loans = Arrays.asList(loan);

        long bookId = 1;

        when(service.findLoans(bookId)).thenReturn(loans);

        // when
        ResultActions result = mockMvc.perform(get("/loans?bookId=1"));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].editionId", is(1)))
                //.andExpect(jsonPath("$[0].dateLent", is(LocalDate.now())))
                //.andExpect(jsonPath("$[0].dateToReturn", is(LocalDate.now().plusDays(14))))
                .andExpect(jsonPath("$[0].returned", is(false)))
                .andExpect(jsonPath("$[0].overdue", is(false)));
    }

    @Test
    public void shouldGetLoansAllSuccess() throws Exception {
        // given
        long userId = 1;
        long editionId = 1;
        Loan loan = getNewLoan(userId, editionId);
        List<Loan> loans = Arrays.asList(loan);

        when(service.findLoans()).thenReturn(loans);

        // when
        ResultActions result = mockMvc.perform(get("/loans"));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].editionId", is(1)))
                //.andExpect(jsonPath("$[0].dateLent", is(LocalDate.now())))
                //.andExpect(jsonPath("$[0].dateToReturn", is(LocalDate.now().plusDays(14))))
                .andExpect(jsonPath("$[0].returned", is(false)))
                .andExpect(jsonPath("$[0].overdue", is(false)));
    }

    @Test
    public void shouldLendSuccess() throws Exception {
        // given
        long user1Id = 1;
        long editionId = 1;
        Loan loan = getNewLoan(user1Id, editionId);
        LoanResource resource = getLoanResource(user1Id, editionId);

        when(creationValidator.validate(resource)).thenReturn(new ErrorsResource(new ArrayList<>()));
        when(service.registerLoan(resource)).thenReturn(loan);

        // when
        ResultActions result = mockMvc.perform(post("/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(resource)));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.editionId", is(1)))
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
        LoanResource resource = getLoanResource(userId, editionId);

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
        long userId = 1;
        long editionId = 1;
        Loan loan = getNewLoan(userId, editionId);
        LoanResource resource = new LoanResource(loan);
        Loan loanReturned = getNewLoanReturnedMock(loanId, userId, editionId);

        when(service.loanExists(loanId)).thenReturn(true);
        when(service.findLoan(loanId)).thenReturn(loan);
        when(returnValidator.validate(resource)).thenReturn(new ErrorsResource(new ArrayList<>()));
        when(service.returnLoan(loanId)).thenReturn(loanReturned);

        // when
        ResultActions result = mockMvc.perform(put("/loans/{loanId}", loanId));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.editionId", is(1)))
                .andExpect(jsonPath("$.returned", is(true)))
                .andExpect(jsonPath("$.overdue", is(false)));
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
        long userId = 1;
        long editionId = 1;
        Loan loan = getNewLoanReturnedMock(loanId, userId, editionId);
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

}