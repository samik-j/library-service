package com.example.library.domain.user;

import com.example.library.web.user.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User registerUser(UserResource resource) {
        User user = new User(resource.getFirstName(), resource.getLastName());

        return repository.save(user);
    }

    public List<User> findUsers(String lastName) {
        return repository.findUsers(lastName);
    }

    public User updateUser(long userId, UserResource resource) {
        User user = repository.findOne(userId);
        user.updateLastName(resource.getLastName());

        return repository.save(user);
    }

    public User findUser(long userId) {
        return repository.findOne(userId);
    }

    public boolean userExists(long userId) {
        return repository.exists(userId);
    }

    public boolean hasNoSuchUser(UserResource resource) {
        return !repository.existsByFirstNameAndLastName(resource.getFirstName(), resource.getLastName());
    }

    public boolean canBorrow(long userId) {
        return repository.findOne(userId).canBorrow();
    }
}
