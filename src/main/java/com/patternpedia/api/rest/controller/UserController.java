package com.patternpedia.api.rest.controller;

import com.patternpedia.api.entities.user.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(operationId = "getAllUsers", responses = {@ApiResponse(responseCode = "200")}, description = "Retrieve all users")
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
    @Operation(operationId = "createUser", responses = {@ApiResponse(responseCode = "200")}, description = "Create a user")
    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntity newUser(@RequestBody UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
    @Operation(operationId = "updateUser", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Update a user")
    @PutMapping(value = "/{userId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    UserEntity updateUser(@PathVariable UUID userId, @RequestBody UserEntity user) {
        return this.userService.updateUser(user);
    }

    /**
     * DELETE Methods
     */
    @Operation(operationId = "deleteUser", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Delete a user")
    @DeleteMapping(value = "/{userId}")
    void deleteUser(@PathVariable UUID userId) {
        this.userService.deleteUser(userId);
    }
}
