package com.example.library.web.user;

import com.example.library.domain.user.User;
import com.example.library.domain.user.UserService;
import com.example.library.web.ErrorsResource;
import com.example.library.web.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private UserService service;
    private UserCreationValidator validator;

    @Autowired
    public UserController(UserService service, UserCreationValidator validator) {
        this.service = service;
        this.validator = validator;
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
    public ResponseEntity<Object> addUser(@RequestBody UserResource resource) {
        LOGGER.info("User added: firstName: {}, lastName: {}",
                resource.getFirstName(), resource.getLastName());

        ErrorsResource errorsResource = validator.validate(resource);

        if(errorsResource.getValidationErrors().isEmpty()) {
            User user = service.registerUser(resource);

            return new ResponseEntity<Object>(getUserResource(user), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<Object>(errorsResource, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{userId}")
    public UserResource updateUser(@PathVariable long userId, @RequestBody UserResource resource) {

        if(service.userExists(userId)) {
            User user = service.updateUser(userId, resource);

            return getUserResource(user);
        }
        else {
            throw new ResourceNotFoundException();
        }
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
