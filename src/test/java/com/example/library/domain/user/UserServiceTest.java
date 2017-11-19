package com.example.library.domain.user;


import com.example.library.web.user.UserResource;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserRepository repository = mock(UserRepository.class);
    private UserService service = new UserService(repository);

    private UserResource getResource() {
        UserResource resource = new UserResource();
        resource.setFirstName("First");
        resource.setLastName("Last");

        return resource;
    }

    @Test
    public void shouldRegisterUser() {
        // given
        UserResource resource = getResource();
        User user = new User(resource.getFirstName(), resource.getLastName());

        when(repository.save(user)).thenReturn(user);

        // when
        User registered = service.registerUser(resource);

        // then
        assertEquals(resource.getFirstName(), registered.getFirstName());
        assertEquals(resource.getLastName(), registered.getLastName());
        assertEquals(LocalDate.now(), registered.getDateJoined());
        assertEquals(0, registered.getBorrowed());
    }

    @Test
    public void shouldUpdateUser() {
        // given
        UserResource resource = new UserResource();
        resource.setLastName("LastNameUpdated");
        User userToUpdate = new User("First", "OldLastName");
        User userUpdated = new User("First", resource.getLastName());
        long userId = 1;

        when(repository.findOne(userId)).thenReturn(userToUpdate);
        when(repository.save(userToUpdate)).thenReturn(userUpdated);

        // when
        User result = service.updateUser(userId, resource);

        // then
        assertEquals("First", result.getFirstName());
        assertEquals(resource.getLastName(), result.getLastName());
    }

    @Test
    public void shouldFindUsersAll() {
        // given
        User user1 = new User("First", "Last");
        User user2 = new User("First2", "Someone");
        List<User> users = Arrays.asList(user1, user2);

        String lastName = null;

        when(repository.findUsers(lastName)).thenReturn(users);

        // when
        List<User> result = service.findUsers(lastName);

        // then
        assertEquals(2, result.size());
        assertEquals(user1, result.get(0));
        assertEquals(user2, result.get(1));
    }

    @Test
    public void shouldFindUsersByLastName() {
        // given
        User user1 = new User("First", "Last");
        List<User> users = Arrays.asList(user1);

        String lastName = "Last";

        when(repository.findUsers(lastName)).thenReturn(users);

        // when
        List<User> result = service.findUsers(lastName);

        // then
        assertEquals(1, result.size());
        assertEquals(user1, result.get(0));
    }

    @Test
    public void shouldFindUserById() {
        // given
        User user = new User("First", "Last");

        long userId = 1;

        when(repository.findOne(userId)).thenReturn(user);

        // when
        User result = service.findUser(userId);

        // then
        assertEquals(user, result);
    }

    @Test
    public void shouldUserExist() {
        // given
        long userId = 1;

        when(repository.exists(userId)).thenReturn(true);

        // when
        boolean result = service.userExists(userId);

        // then
        assertTrue(result);
    }

    @Test
    public void userExistsShouldReturnFalseIfUserNotExists() {
        // given
        UserResource resource = getResource();

        when(repository.existsByFirstNameAndLastName(resource.getFirstName(), resource.getLastName()))
                .thenReturn(false);

        // when
        boolean result = service.userExists(resource);

        // then
        assertFalse(result);
    }

    @Test
    public void userExistsShouldReturnTrueIfUserExists() {
        // given
        UserResource resource = getResource();

        when(repository.existsByFirstNameAndLastName(resource.getFirstName(), resource.getLastName()))
                .thenReturn(true);

        // when
        boolean result = service.userExists(resource);

        // then
        assertTrue(result);
    }

    @Test
    public void canBorrowShouldReturnTrueIfUserCanBorrow() {
        // given
        User user = new User("First", "Last");

        long userId = 1;

        when(repository.findOne(userId)).thenReturn(user);

        // when
        boolean result = service.canBorrow(userId);

        // then
        assertTrue(result);
    }

    @Test
    public void canBorrowShouldReturnFalseIfUserCanNotBorrow() {
        // given
        User user = mock(User.class);

        long userId = 1;

        when(user.canBorrow()).thenReturn(false);
        when(repository.findOne(userId)).thenReturn(user);

        // when
        boolean result = service.canBorrow(userId);

        // then
        assertFalse(result);
    }

    @Test
    public void canReturnShouldReturnTrueIfUserCanReturn() {
        // given
        User user = mock(User.class);

        long userId = 1;

        when(user.canReturn()).thenReturn(true);
        when(repository.findOne(userId)).thenReturn(user);

        // when
        boolean result = service.canReturn(userId);

        // then
        assertTrue(result);
    }

    @Test
    public void canReturnShouldReturnFalseIfUserCanNotReturn() {
        // given
        User user = mock(User.class);

        long userId = 1;

        when(user.canReturn()).thenReturn(false);
        when(repository.findOne(userId)).thenReturn(user);

        // when
        boolean result = service.canReturn(userId);

        // then
        assertFalse(result);
    }

}