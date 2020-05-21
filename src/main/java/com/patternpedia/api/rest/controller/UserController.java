package com.patternpedia.api.rest.controller;

import com.patternpedia.api.entities.user.UserRole;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/users", produces = "application/hal+json")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private ObjectMapper objectMapper;

    public UserController(
            UserService userService,
            PasswordEncoder passwordEncoder,
            ObjectMapper objectMapper
    ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }

    /**
     * GET Methods
     */
    @GetMapping(value = "")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    List<UserEntity> all() {
        return this.userService.getAllUsers();
    }

    @GetMapping(value = "/{userId}")
    UserEntity getUserById(@PathVariable UUID userId) {
        return this.userService.getUserById(userId);
    }

    /**
     * CREATE Methods
     */
    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntity newUser(@RequestBody UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.userService.createUser(user);

    }

    /**
     * UPDATE Methods
     */
    @PutMapping(value = "/{userId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    UserEntity updateUser(@PathVariable UUID userId, @RequestBody UserEntity user) {
        return this.userService.updateUser(user);
    }

    /**
     * DELETE Methods
     */
    @DeleteMapping(value = "/{userId}")
    void deleteUser(@PathVariable UUID userId) {
        this.userService.deleteUser(userId);
    }
}
