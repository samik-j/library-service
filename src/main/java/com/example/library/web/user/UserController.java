package com.example.library.web.user;

import com.example.library.domain.user.User;
import com.example.library.domain.user.UserService;
import com.example.library.web.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<UserResource> getUsers(@RequestParam(required = false) String lastName) {
        LOGGER.info("Users filtered: lastName: {}", lastName);

        return getUserResources(service.findUsers(lastName));
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public UserResource getUser(@PathVariable long userId) {
        User user = service.findUser(userId);

        if(user != null) {
            return getUserResource(user);
        }
        else {
            throw new ResourceNotFoundException();
        }

    }

    @RequestMapping(method = RequestMethod.POST)
    public UserResource addUser(@RequestBody UserResource resource) {
        LOGGER.info("User added: firstName:{}, laseName:{}",
                resource.getFirstName(), resource.getLastName());
        User user = service.registerUser(resource);

        return getUserResource(user);
    }

    @PutMapping("/{userId}")
    public UserResource updateUser(@PathVariable long userId, @RequestBody UserResource resource) {
        User user = service.updateUser(userId, resource);

        return getUserResource(user);
    }

    private UserResource getUserResource(User user) {
        return new UserResource(user);
    }

    private List<UserResource> getUserResources(Collection<User> users) {
        List<UserResource> userResources = new ArrayList<>();

        for(User user : users) {
            userResources.add(new UserResource(user));
        }

        return userResources;
    }
}
