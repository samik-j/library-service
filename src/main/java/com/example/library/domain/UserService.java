package com.example.library.domain;

import com.example.library.web.UserResource;
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
        if(lastName != null) {
            return repository.findAllByLastName(lastName);
        }
        else {
            return repository.findAll();
        }
    }
}
