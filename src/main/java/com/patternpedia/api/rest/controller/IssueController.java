package com.patternpedia.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patternpedia.api.rest.model.issue.IssueModelRequest;
import com.patternpedia.api.rest.model.shared.CommentModel;
import com.patternpedia.api.rest.model.issue.IssueModel;
import com.patternpedia.api.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/issues", produces = "application/hal+json")
public class IssueController {

    Logger logger = LoggerFactory.getLogger(IssueController.class);

    private IssueService issueService;
    private PatternLanguageService patternLanguageService;
    private ObjectMapper objectMapper;

    public IssueController(
            IssueService issueService,
            PatternLanguageService patternLanguageService,
            ObjectMapper objectMapper
    ) {
        this.issueService = issueService;
        this.patternLanguageService = patternLanguageService;
        this.objectMapper = objectMapper;
    }

    /**
     * GET Methods
     */
    @GetMapping(value = "")
    CollectionModel<EntityModel<IssueModel>> getAll() {
        List<EntityModel<IssueModel>> issues = this.issueService.getAllIssues()
                .stream()
                .map(issue -> new EntityModel<>(new IssueModel(issue)))
                .collect(Collectors.toList());
        return new CollectionModel<>(issues);
    }

    @GetMapping(value = "/{issueId}")
    @PreAuthorize(value = "#oauth2.hasScope('read')")
    ResponseEntity<EntityModel<IssueModel>> getIssueById(@PathVariable UUID issueId) {
        return ResponseEntity.ok(new EntityModel<>(new IssueModel(this.issueService.getIssueById(issueId))));
    }

    @GetMapping(value = "/?uri={issueUri}")
    ResponseEntity<EntityModel<IssueModel>> getIssueByUri(@PathVariable String issueUri) {
        return ResponseEntity.ok(new EntityModel<>(new IssueModel(this.issueService.getIssueByURI(issueUri))));
    }

    /**
     * CREATE Methods
     */
    @PostMapping(value = "")
    @PreAuthorize(value = "#oauth2.hasScope('write')")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<IssueModel>> newIssue(@RequestBody IssueModelRequest issueModelRequest, @AuthenticationPrincipal Principal principal) {
        return ResponseEntity.ok(new EntityModel<>(new IssueModel(this.issueService.createIssue(issueModelRequest, UUID.fromString(principal.getName())))));
    }

    @PostMapping(value = "/{issueId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<CommentModel>> newIssueComment(@PathVariable UUID issueId, @AuthenticationPrincipal Principal principal, @RequestBody CommentModel commentModel) {
        return ResponseEntity.ok(new EntityModel<>(new CommentModel(this.issueService.createComment(issueId, UUID.fromString(principal.getName()), commentModel))));
    }

    /**
     * UPDATE Methods
     */
    @PutMapping(value = "/{issueId}")
    ResponseEntity<EntityModel<IssueModel>> putIssue(@PathVariable UUID issueId, @AuthenticationPrincipal Principal principal, @RequestBody IssueModelRequest issueModelRequest) {
        return ResponseEntity.ok(new EntityModel<>(new IssueModel(this.issueService.updateIssue(issueId, UUID.fromString(principal.getName()), issueModelRequest))));
    }

    @PutMapping(value = "/{issueId}/comments/{issueCommentId}")
    ResponseEntity<EntityModel<CommentModel>> putIssueCommentRating(@PathVariable UUID issueId, @PathVariable UUID issueCommentId, @AuthenticationPrincipal Principal principal, @RequestBody CommentModel commentModel) {
        return ResponseEntity.ok(new EntityModel<>(new CommentModel(this.issueService.updateComment(issueId, issueCommentId, UUID.fromString(principal.getName()), commentModel))));
    }

    /**
     * DELETE Methods
     */
    @DeleteMapping(value = "/{issueId}")
    ResponseEntity<?> deleteIssue(@PathVariable UUID issueId) {
        this.issueService.deleteIssue(issueId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{issueId}/comments/{issueCommentId}")
    ResponseEntity<?> deleteComment(@PathVariable UUID issueId, @PathVariable UUID issueCommentId, @AuthenticationPrincipal Principal principal) {
        return this.issueService.deleteComment(issueId, issueCommentId, UUID.fromString(principal.getName()));
    }
}
