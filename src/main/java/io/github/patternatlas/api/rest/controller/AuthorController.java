package com.patternpedia.api.rest.controller;

import com.patternpedia.api.entities.shared.AuthorConstant;
import com.patternpedia.api.rest.model.candidate.CandidateModel;
import com.patternpedia.api.rest.model.issue.IssueModel;
import com.patternpedia.api.rest.model.shared.AuthorModel;
import com.patternpedia.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/authors", produces = "application/hal+json")
public class AuthorController {

    private UserService userService;

    public AuthorController(
            UserService userService
    ) {
        this.userService = userService;
    }

    /**
     * GET Methods
     */
    @Operation(operationId = "getallUsersAsAuthors", responses = {@ApiResponse(responseCode = "200")}, description = "Retrieve all authors")
    @GetMapping(value = "")
    CollectionModel<EntityModel<AuthorModel>> getAll() {
        List<EntityModel<AuthorModel>> authors = this.userService.getAllUsers()
                .stream()
                .map(user -> new EntityModel<>(new AuthorModel(user)))
                .collect(Collectors.toList());
        return new CollectionModel<>(authors);
    }

    @Operation(operationId = "getAllAuthorRoles", responses = {@ApiResponse(responseCode = "200")}, description = "Retrieve all author roles")
    @GetMapping(value = "/roles")
    String[] getAllRoles() {
        return new String[]{AuthorConstant.MEMBER, AuthorConstant.MAINTAINER, AuthorConstant.OWNER};
    }
}
