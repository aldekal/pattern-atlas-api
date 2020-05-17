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
@RequestMapping(value = "/user", produces = "application/hal+json")
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
    @GetMapping(value = "/getAll")
//    @PreAuthorize(value = "hasAuthority('MEMBER')")
    List<UserEntity> all() {
        return this.userService.getAllUsers();
    }

    @GetMapping(value = "/getById/{userId}")
    UserEntity getUserById(@PathVariable UUID userId) {
        return this.userService.getUserById(userId);
    }

    @GetMapping(value = "/getUser")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<Principal> get(final Principal principal) {
        return ResponseEntity.ok(principal);
    }


    /**
     * CREATE Methods
     */
    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntity newUser(@RequestBody UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        logger.info(user.getPassword());
        return this.userService.createUser(user);

    }

    public void defaultUsers() {
        List <UserRole> role = new ArrayList<>(Arrays.asList(UserRole.MEMBER));
        UserEntity userMember = new UserEntity("Member User", "member@mail", passwordEncoder.encode("pass"), role);
        this.userService.createUser(userMember);
        role.add(UserRole.ADMIN);
        UserEntity userAdmin = new UserEntity("Admin User", "admin@mail", passwordEncoder.encode("pass"), role);
        this.userService.createUser(userAdmin);
    }

    /**
     * UPDATE Methods
     */
    @PutMapping(value = "/update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    UserEntity updateUser(@RequestBody UserEntity user) {
        logger.info(user.toString());
        return this.userService.updateUser(user);
    }

    /**
     * DELETE Methods
     */
    @DeleteMapping(value = "/delete/{userId}")
    void deleteUser(@PathVariable UUID userId) {
        this.userService.deleteUser(userId);
    }
}
