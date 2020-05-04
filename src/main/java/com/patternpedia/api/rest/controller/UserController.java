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
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/user", produces = "application/hal+json")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
//    private PatternLanguageService patternLanguageService;
//    private PatternViewService patternViewService;
//    private PatternRelationDescriptorService patternRelationDescriptorService;
    private ObjectMapper objectMapper;

    public UserController(
            UserService userService,
            PasswordEncoder passwordEncoder,
//            PatternLanguageService patternLanguageService,
//            PatternViewService patternViewService,
//            PatternRelationDescriptorService patternRelationDescriptorService,
            ObjectMapper objectMapper
    ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
//        this.patternLanguageService = patternLanguageService;
//        this.patternViewService = patternViewService;
//        this.patternRelationDescriptorService = patternRelationDescriptorService;
        this.objectMapper = objectMapper;
    }

    /**
     * GET Methods
     */
//    @CrossOrigin(origins = "http://localhost:4200")
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
//    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntity newUser(@RequestParam String name, @RequestParam String mail, @RequestParam String password) {
        UserEntity user = new UserEntity(name, mail, passwordEncoder.encode(password));
//        UserEntity user = new UserEntity(name, mail, password);
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
    @PutMapping(value = "/update/{userId}")
    UserEntity updateUser(@PathVariable UUID userId, @RequestBody UserEntity user) {
        user.setId(userId);
        logger.info(user.toString());
        return this.userService.updateUser(user);
    }

    /**
     * DELETE Methods
     */
    @DeleteMapping(value = "/delete/{userId}")
    void deleteUser(@PathVariable UUID userId) {
        this.userService.deleteUser(userId);
//        return ResponseEntity.noContent().build();
    }
}
