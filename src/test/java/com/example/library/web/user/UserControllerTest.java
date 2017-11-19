package com.example.library.web.user;

import com.example.library.domain.user.User;
import com.example.library.domain.user.UserService;
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

import java.util.ArrayList;
import java.util.Arrays;
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
    private UserCreationValidator creationValidator;

    @Mock
    private UserUpdateValidator updateValidator;

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
        UserController controller = new UserController(service, creationValidator, updateValidator);
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
                //.andExpect(jsonPath("$[0].dateJoined", is(LocalDate.now())))
                .andExpect(jsonPath("$[0].borrowed", is(0)))
                .andExpect(jsonPath("$[1].firstName", is("FirstName")))
                .andExpect(jsonPath("$[1].lastName", is("LastName")))
                //.andExpect(jsonPath("$[1].dateJoined", is(LocalDate.now())))
                .andExpect(jsonPath("$[1].borrowed", is(0)));
    }

    @Test
    public void shouldGetAllUsersByLastNameSuccess() throws Exception {
        // given
        List<User> users = new ArrayList<>();
        users.add(new User("FirstName", "LastName"));

        String lastName = "LastName";

        when(service.findUsers(lastName)).thenReturn(users);

        // when
        ResultActions result = mockMvc.perform(get("/users?lastName=LastName"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("FirstName")))
                .andExpect(jsonPath("$[0].lastName", is("LastName")))
                //.andExpect(jsonPath("$[0].dateJoined", is(LocalDate.now())))
                .andExpect(jsonPath("$[0].borrowed", is(0)));
    }

    @Test
    public void shouldGetUserSuccess() throws Exception {
        // given
        User user = new User("First", "Last");
        long userId = 1;

        when(service.findUser(userId)).thenReturn(user);

        // when
        ResultActions result = mockMvc.perform(get("/users/{userId}", userId));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.firstName", is("First")))
                .andExpect(jsonPath("$.lastName", is("Last")))
                //.andExpect(jsonPath("$[0].dateJoined", is(LocalDate.now())))
                .andExpect(jsonPath("$.borrowed", is(0)));
    }

    @Test
    public void shouldGetUserByIdFail404NotFound() throws Exception {
        // given
        long userId = 1;

        when(service.findUser(userId)).thenReturn(null);

        // when
        ResultActions result = mockMvc.perform(get("/users/{userId}", userId));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void shouldAddUserSuccess() throws Exception {
        // given
        UserResource resource = createUserResource("First", "Last");
        User user = new User("First", "Last");

        when(creationValidator.validate(resource)).thenReturn(new ErrorsResource(new ArrayList<>()));
        when(service.registerUser(resource)).thenReturn(user);

        // when
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(resource)));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.firstName", is("First")))
                .andExpect(jsonPath("$.lastName", is("Last")))
                //.andExpect(jsonPath("$[0].dateJoined", is(LocalDate.now())))
                .andExpect(jsonPath("$.borrowed", is(0)));
    }

    @Test
    public void shouldAddUserFail400BadRequest() throws Exception {
        // given
        UserResource resource = createUserResource("First", "Last");

        when(creationValidator.validate(resource)).thenReturn(new ErrorsResource(Arrays.asList("User already exists")));

        // when
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(resource)));

        // then
        result.andExpect(status().isBadRequest());

        verify(creationValidator, times(1)).validate(resource);
        verify(service, times(0)).registerUser(resource);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void shouldUpdateUserSuccess() throws Exception {
        // given
        UserResource resource = createUserResource("First", "Last2");
        User userUpdated = new User("First", "Last2");
        long userId = 1;

        when(service.userExists(userId)).thenReturn(true);
        when(updateValidator.validate(resource)).thenReturn(new ErrorsResource(new ArrayList<>()));
        when(service.updateUser(userId, resource)).thenReturn(userUpdated);

        // when
        ResultActions result = mockMvc.perform(put("/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(resource)));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.firstName", is("First")))
                .andExpect(jsonPath("$.lastName", is("Last2")))
                //.andExpect(jsonPath("$[0].dateJoined", is(LocalDate.now())))
                .andExpect(jsonPath("$.borrowed", is(0)));
    }

    @Test
    public void shouldUpdateUserFail404NotFound() throws Exception {
        // given
        UserResource resource = createUserResource("First", "Last2");
        long userId = 1;

        when(service.userExists(userId)).thenReturn(false);

        // when
        ResultActions result = mockMvc.perform(put("/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(resource)));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateUserFailIfParameterIsEmpty400BadRequest() throws Exception {
        // given
        UserResource resource = createUserResource("First", "");
        long userId = 1;

        when(service.userExists(userId)).thenReturn(true);
        when(updateValidator.validate(resource)).thenReturn(new ErrorsResource(Arrays.asList("Last name not specified")));

        // when
        ResultActions result = mockMvc.perform(put("/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(resource)));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.validationErrors", is(Arrays.asList("Last name not specified"))));

        verify(service, times(1)).userExists(userId);
        verify(updateValidator, times(1)).validate(resource);
        verify(service, times(0)).updateUser(userId, resource);
        verifyNoMoreInteractions(service);
    }

    private UserResource createUserResource(String firstName, String lastName) {
        User user = new User(firstName, lastName);

        return new UserResource(user);
    }
}