package io.github.patternatlas.api.rest.controller;

import io.github.patternatlas.api.config.Authority;
import io.github.patternatlas.api.rest.model.user.*;
import io.github.patternatlas.api.service.UserService;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @PreAuthorize(value = Authority.USER_READ_ALL)
    CollectionModel<EntityModel<UserModel>> getAll() {
        List<EntityModel<UserModel>> users = this.userService.getAllUsers()
                .stream()
                .map(user -> new EntityModel<>(new UserModel(user)))
                .collect(Collectors.toList());
        return new CollectionModel<>(users);
    }

    @GetMapping(value = "/{userId}")
    ResponseEntity<EntityModel<UserModel>> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(new EntityModel<>(new UserModel(this.userService.getUserById(userId))));
    }

    @GetMapping(value = "/roles")
    CollectionModel<EntityModel<RoleModel>>  getAllRoles() {
        List<EntityModel<RoleModel>> roles = this.userService.getAllRoles()
                .stream()
                .map(role -> new EntityModel<>(new RoleModel(role)))
                .collect(Collectors.toList());
        return new CollectionModel<>(roles);
    }

    @GetMapping(value = "/roles/privileges")
    CollectionModel<EntityModel<PrivilegeModel>>  getAllPrivileges() {
        List<EntityModel<PrivilegeModel>> roles = this.userService.getAllPrivileges()
                .stream()
                .map(privilege -> new EntityModel<>(new PrivilegeModel(privilege)))
                .collect(Collectors.toList());
        return new CollectionModel<>(roles);
    }

    /**
     * CREATE Methods
     */
    @Operation(operationId = "createUser", responses = {@ApiResponse(responseCode = "200")}, description = "Create a user")
    @PostMapping(value = "")
    @PreAuthorize(value = Authority.USER_CREATE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<UserModel>> newUser(@RequestBody UserModelRequest userModelRequest) {
        return ResponseEntity.ok(new EntityModel<>(new UserModel(this.userService.createUser(userModelRequest))));
    }

    /**
     * UPDATE Methods
     */
    @Operation(operationId = "updateUser", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404", content = @Content)}, description = "Update a user")
    @PutMapping(value = "/{userId}")
    @PreAuthorize(value = Authority.USER_EDIT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<EntityModel<UserModel>> updateUser(@PathVariable UUID userId, @RequestBody UserModelRequest userModelRequest) {
        return ResponseEntity.ok(new EntityModel<>(new UserModel(this.userService.updateUser(userId, userModelRequest))));
    }

    @PutMapping(value = "/roles/{roleId}/privileges/{privilegeId}")
    @PreAuthorize(value = Authority.USER_READ_ALL)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<EntityModel<RoleModel>> updateUserRole(@PathVariable UUID roleId, @PathVariable UUID privilegeId, @RequestBody RoleModelRequest roleModelRequest) {
        return ResponseEntity.ok(new EntityModel<>(new RoleModel(this.userService.updateRole(roleId, privilegeId, roleModelRequest))));
    }

    /**
     * DELETE Methods
     */
    @Operation(operationId = "deleteUser", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404", content = @Content)}, description = "Delete a user")
    @DeleteMapping(value = "/{userId}")
    @PreAuthorize(value = Authority.USER_DELETE)
    ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
        this.userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
