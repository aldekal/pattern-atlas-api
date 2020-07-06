package com.patternpedia.api.rest.controller;

import com.patternpedia.api.entities.shared.AuthorConstant;
import com.patternpedia.api.rest.model.candidate.CandidateModel;
import com.patternpedia.api.rest.model.issue.IssueModel;
import com.patternpedia.api.rest.model.shared.AuthorModel;
import com.patternpedia.api.rest.model.shared.AuthorModelRequest;
import com.patternpedia.api.rest.model.user.UserModel;
import com.patternpedia.api.service.AuthorService;
import com.patternpedia.api.service.UserService;
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

    private AuthorService authorService;
    private UserService userService;

    public AuthorController(
            AuthorService authorService,
            UserService userService
    ) {
        this.authorService = authorService;
        this.userService = userService;
    }

    /**
     * GET Methods
     */
    @GetMapping(value = "")
    CollectionModel<EntityModel<AuthorModel>> getAll() {
        List<EntityModel<AuthorModel>> authors = this.userService.getAllUsers()
                .stream()
                .map(user -> new EntityModel<>(new AuthorModel(user)))
                .collect(Collectors.toList());
        return new CollectionModel<>(authors);
    }

    @GetMapping(value = "/roles")
    String[] getAllRoles() {
        return new String[]{AuthorConstant.MEMBER, AuthorConstant.MAINTAINER, AuthorConstant.OWNER};
    }

    /**
     * CREATE Methods
     */
    @PostMapping(value = "/{userId}/issues/{issueId}")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<AuthorModel>> newIssueAuthor(@PathVariable UUID userId, @PathVariable UUID issueId, @RequestBody AuthorModelRequest authorModelRequest) {
        return ResponseEntity.ok(new EntityModel<>(new AuthorModel(this.authorService.saveIssueAuthor(userId, issueId, authorModelRequest))));
    }

    @PostMapping(value = "/{userId}/candidates/{candidateId}")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<AuthorModel>> newCandidateAuthor(@PathVariable UUID userId, @PathVariable UUID candidateId, @RequestBody AuthorModelRequest authorModelRequest) {
        return ResponseEntity.ok(new EntityModel<>(new AuthorModel(this.authorService.saveCandidateAuthor(userId, candidateId, authorModelRequest))));
    }

    /**
     * UPDATE Methods
     */
    @PutMapping(value = "/{userId}/issues/{issueId}")
    ResponseEntity<EntityModel<AuthorModel>> putIssueAuthor(@PathVariable UUID userId, @PathVariable UUID issueId, @RequestBody AuthorModelRequest authorModelRequest) {
        return ResponseEntity.ok(new EntityModel<>(new AuthorModel(this.authorService.saveIssueAuthor(userId, issueId, authorModelRequest))));
    }

    @PutMapping(value = "/{userId}/candidates/{candidateId}")
    ResponseEntity<EntityModel<AuthorModel>> putCandidateAuthor(@PathVariable UUID userId, @PathVariable UUID candidateId, @RequestBody AuthorModelRequest authorModelRequest) {
        return ResponseEntity.ok(new EntityModel<>(new AuthorModel(this.authorService.saveCandidateAuthor(userId, candidateId, authorModelRequest))));
    }

    /**
     * DELETE Methods
     */
    @DeleteMapping(value = "/{userId}/issues/{issueId}")
    ResponseEntity<?> deleteIssueAuthor(@PathVariable UUID userId, @PathVariable UUID issueId) {
        this.authorService.deleteIssueAuthor(userId, issueId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{userId}/candidates/{candidateId}")
    ResponseEntity<?> deleteCandidateAuthor(@PathVariable UUID userId, @PathVariable UUID candidateId) {
        this.authorService.deleteCandidateAuthor(userId, candidateId);
        return ResponseEntity.noContent().build();
    }
}
