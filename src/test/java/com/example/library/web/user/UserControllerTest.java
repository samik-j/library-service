package com.example.library.web.user;

import com.example.library.domain.user.User;
import com.example.library.domain.user.UserService;
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
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService service;

    @Mock
    private UserCreationValidator validator;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        UserController controller = new UserController(service, validator);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .addFilters(new CorsFilter())
                .build();
    }

    @Test
    public void shouldGetAllUsersSuccess() throws Exception {
        // given
        List<User> users = new ArrayList<>();
        users.add(new User("First", "Last"));
        users.add(new User("FirstName", "LastName"));

        String lastName = null;

        when(service.findUsers(lastName)).thenReturn(users);

        // when
        ResultActions result = mockMvc.perform(get("/users"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is("First")))
                .andExpect(jsonPath("$[0].lastName", is("Last")))
                .andExpect(jsonPath("$[0].dateJoined", is(LocalDate.now())))
                .andExpect(jsonPath("$[0].borrowed", is(0)))
                .andExpect(jsonPath("$[1].firstName", is("FirstName")))
                .andExpect(jsonPath("$[1].lastName", is("LastName")))
                //.andExpect(jsonPath("$[1].dateJoined", is(LocalDate.now())))
                .andExpect(jsonPath("$[1].borrowed", is(0)));
    }

    private static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}