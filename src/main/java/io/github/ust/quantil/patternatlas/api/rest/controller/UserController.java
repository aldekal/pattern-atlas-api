package io.github.ust.quantil.patternatlas.api.rest.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.ust.quantil.patternatlas.api.entities.user.UserEntity;
import io.github.ust.quantil.patternatlas.api.entities.user.UserRole;
import io.github.ust.quantil.patternatlas.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value = "/users", produces = "application/hal+json")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

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
        List<UserRole> role = new ArrayList<>(Arrays.asList(UserRole.MEMBER));
        UserEntity userMember = new UserEntity("Member User", "member@mail", passwordEncoder.encode("pass"), role);
        this.userService.createUser(userMember);
        role.add(UserRole.ADMIN);
        UserEntity userAdmin = new UserEntity("Admin User", "admin@mail", passwordEncoder.encode("pass"), role);
        this.userService.createUser(userAdmin);
    }

    /**
     * UPDATE Methods
     */
    @Operation(operationId = "updateUser", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404", content = @Content)}, description = "Update a user")
    @PutMapping(value = "/{userId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    UserEntity updateUser(@PathVariable UUID userId, @RequestBody UserEntity user) {
        return this.userService.updateUser(user);
    }

    /**
     * DELETE Methods
     */
    @Operation(operationId = "deleteUser", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404", content = @Content)}, description = "Delete a user")
    @DeleteMapping(value = "/{userId}")
    void deleteUser(@PathVariable UUID userId) {
        this.userService.deleteUser(userId);
    }
}
