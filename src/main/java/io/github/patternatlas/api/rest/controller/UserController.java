package io.github.patternatlas.api.rest.controller;

import io.github.patternatlas.api.rest.model.user.*;
import io.github.patternatlas.api.service.UserService;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
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

    // Exposes whole activity of each user (including created issues, candidates...). This should only be
    // allowed for USER_READ_ALL, or USER_READ if it is for user themselves
    @Operation(operationId = "getAllUsers", responses = {@ApiResponse(responseCode = "200")}, description = "Retrieve all users")
    @GetMapping(value = "")
    @PostFilter(value = "hasGlobalPermission(@PC.USER_READ_ALL) or " +
            "(hasGlobalPermission(@PC.USER_READ) and filterObject.getContent().id.equals(loggedInUUID()))")
    CollectionModel<EntityModel<UserModel>> getAll() {
        List<EntityModel<UserModel>> users = this.userService.getAllUsers()
                .stream()
                .map(user -> new EntityModel<>(new UserModel(user)))
                .collect(Collectors.toList());
        // and )
        return CollectionModel.of(users);
    }

    @GetMapping(value = "/{userId}")
    ResponseEntity<EntityModel<UserModel>> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(EntityModel.of(new UserModel(this.userService.getUserById(userId))));
    }

    @GetMapping(value = "/roles")
    CollectionModel<EntityModel<RoleModel>>  getAllRoles() {
        List<EntityModel<RoleModel>> roles = this.userService.getAllRoles()
                .stream()
                .map(role -> EntityModel.of(new RoleModel(role)))
                .collect(Collectors.toList());
        return CollectionModel.of(roles);
    }

    @GetMapping(value = "/roles/platform")
    CollectionModel<EntityModel<RoleModel>>  getAllPlatformRoles() {
        List<EntityModel<RoleModel>> roles = this.userService.getAllPlatformRoles()
                .stream()
                .map(role -> EntityModel.of(new RoleModel(role)))
                .collect(Collectors.toList());
        return CollectionModel.of(roles);
    }

    @GetMapping(value = "/roles/authors")
    CollectionModel<EntityModel<RoleModel>>  getAllAuthorRoles() {
        List<EntityModel<RoleModel>> roles = this.userService.getAllAuthorRoles()
                .stream()
                .map(role -> EntityModel.of(new RoleModel(role)))
                .collect(Collectors.toList());
        return CollectionModel.of(roles);
    }

    @GetMapping(value = "/roles/{entityId}")
    CollectionModel<EntityModel<RoleModel>>  getAllRolesFromEntity(@PathVariable UUID entityId) {
        List<EntityModel<RoleModel>> roles = this.userService.getAllRolesFromEntity(entityId)
                .stream()
                .map(role -> EntityModel.of(new RoleModel(role)))
                .collect(Collectors.toList());
        return CollectionModel.of(roles);
    }

    @GetMapping(value = "/roles/privileges")
    CollectionModel<EntityModel<PrivilegeModel>>  getAllPlatformPrivileges() {
        List<EntityModel<PrivilegeModel>> privileges = this.userService.getAllPlatformPrivileges()
                .stream()
                .map(privilege -> EntityModel.of(new PrivilegeModel(privilege)))
                .collect(Collectors.toList());
        return CollectionModel.of(privileges);
    }

    @GetMapping(value = "/roles/default_author_privileges")
    CollectionModel<EntityModel<PrivilegeModel>> getAllDefaultAuthorPrivileges() {
        List<EntityModel<PrivilegeModel>> privileges = this.userService.getAllDefaultAuthorPrivileges()
                .stream()
                .map(privilege -> EntityModel.of(new PrivilegeModel(privilege)))
                .collect(Collectors.toList());
        return CollectionModel.of(privileges);
    }

    @GetMapping(value = "/roles/privileges/{entityId}")
    CollectionModel<EntityModel<PrivilegeModel>>  getAllPrivilegesFromEntity(@PathVariable UUID entityId) {
        List<EntityModel<PrivilegeModel>> privileges = this.userService.getAllPrivilegesFromEntity(entityId)
                .stream()
                .map(privilege -> EntityModel.of(new PrivilegeModel(privilege)))
                .collect(Collectors.toList());
        return CollectionModel.of(privileges);
    }

    /**
     * CREATE Methods
     */
    @Operation(operationId = "createUser", responses = {@ApiResponse(responseCode = "200")}, description = "Create a user")
    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<UserModel>> newUser(@RequestBody UserModelRequest userModelRequest) {
        return ResponseEntity.ok(EntityModel.of(new UserModel(this.userService.createUser(userModelRequest))));
    }

    /**
     * UPDATE Methods
     */
    @Operation(operationId = "updateUser", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404", content = @Content)}, description = "Update a user")
    @PutMapping(value = "/{userId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<EntityModel<UserModel>> updateUser(@PathVariable UUID userId, @RequestBody UserModelRequest userModelRequest) {
        return ResponseEntity.ok(EntityModel.of(new UserModel(this.userService.updateUser(userId, userModelRequest))));
    }

    @Operation(operationId = "updatePlatformRole", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404", content = @Content)}, description = "Update platform wide roles for user")
    @PutMapping(value = "/{userId}/role")
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<EntityModel<UserModel>> updatePlatformRole(@PathVariable UUID userId, @RequestBody UserModelRequest userModelRequest) {
        return ResponseEntity.ok(EntityModel.of(new UserModel(this.userService.updatePlatformRole(userId, userModelRequest))));
    }

    @PutMapping(value = "/roles/{roleId}/privileges/{privilegeId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<EntityModel<RoleModel>> updateUserRole(@PathVariable UUID roleId, @PathVariable UUID privilegeId, @RequestBody RoleModelRequest roleModelRequest) {
        return ResponseEntity.ok(EntityModel.of(new RoleModel(this.userService.updateRole(roleId, privilegeId, roleModelRequest))));
    }

    /**
     * This endpoint updates all resource specific roles that are related to the specified author role (HELPER, MAINTAINER or OWNER) according to the value of the specified default author privilege.
     * e.g. the role is 'HELPER', the default author privilege is 'PATTERN_CANDIDATE_EDIT' and the checkboxValue of roleModelRequest is false. This
     * means the privilege 'PATTERN_CANDIDATE_EDIT' should be removed from all 'HELPER' roles from all resources. If a resources has
     * the ID 123, privilege 'PATTERN_CANDIDATE_EDIT_123' will be removed from the role 'HELPER_PATTERN_CANDIDATE_123'
     *
     * @param authorRoleId
     * @param defaultAuthorPrivilegeId
     * @param roleModelRequest
     * @return
     */
    @Operation(description = "Update all resource specific roles according to the value of the specified default author privilege")
    @PutMapping(value = "/roles/{authorRoleId}/privileges/{defaultAuthorPrivilegeId}/all_resource_specific")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void updateAllResourceSpecificAuthorRoles(@PathVariable UUID authorRoleId, @PathVariable UUID defaultAuthorPrivilegeId, @RequestBody RoleModelRequest roleModelRequest) {
        this.userService.updateAllResourceSpecificRoles(authorRoleId, defaultAuthorPrivilegeId, roleModelRequest);
    }

    /**
     * DELETE Methods
     */
    @Operation(operationId = "deleteUser", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404", content = @Content)}, description = "Delete a user")
    @DeleteMapping(value = "/{userId}")
    ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
        this.userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
