package com.patternpedia.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "/user", produces = "application/hal+json")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService userService;
//    private PatternLanguageService patternLanguageService;
//    private PatternViewService patternViewService;
//    private PatternRelationDescriptorService patternRelationDescriptorService;
    private ObjectMapper objectMapper;

    public UserController(
            UserService userService,
//            PatternLanguageService patternLanguageService,
//            PatternViewService patternViewService,
//            PatternRelationDescriptorService patternRelationDescriptorService,
            ObjectMapper objectMapper
    ) {
        this.userService = userService;
//        this.patternLanguageService = patternLanguageService;
//        this.patternViewService = patternViewService;
//        this.patternRelationDescriptorService = patternRelationDescriptorService;
        this.objectMapper = objectMapper;
    }

    /**
     * GET Methods
     */
    @GetMapping(value = "/getAll")
    List<UserEntity> all() {
        return this.userService.getAllUsers();
    }

    @GetMapping(value = "/getById/{userId}")
    UserEntity getUserById(@PathVariable UUID userId) {
        return this.userService.getUserById(userId);
    }

    /**
     * CREATE Methods
     */
    @PostMapping(value = "/create")
//    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    UserEntity newPatternEvolution(@RequestBody UserEntity user) {
        logger.info(user.toString());
        return this.userService.createUser(user);
    }

    /**
     * UPDATE Methods
     */
    @PutMapping(value = "/update/{userId}")
    UserEntity putPatternLanguage(@PathVariable UUID userId, @RequestBody UserEntity user) {
        user.setId(userId);
        logger.info(user.toString());
        return this.userService.updateUser(user);
    }

    /**
     * DELETE Methods
     */
    @DeleteMapping(value = "/delete/{patternEvolutionId}")
    void deletePatternLanguage(@PathVariable UUID userId) {
        this.userService.deleteUser(userId);
//        return ResponseEntity.noContent().build();
    }
}
