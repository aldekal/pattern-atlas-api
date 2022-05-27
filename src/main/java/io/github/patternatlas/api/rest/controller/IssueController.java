package io.github.patternatlas.api.rest.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.github.patternatlas.api.rest.model.issue.IssueModel;
import io.github.patternatlas.api.rest.model.issue.IssueModelRequest;
import io.github.patternatlas.api.rest.model.shared.AuthorModelRequest;
import io.github.patternatlas.api.rest.model.shared.CommentModel;
import io.github.patternatlas.api.rest.model.shared.EvidenceModel;
import io.github.patternatlas.api.rest.model.shared.RatingModelRequest;
import io.github.patternatlas.api.service.IssueService;
import io.github.patternatlas.api.service.PatternLanguageService;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(operationId = "getIssueByURI", responses = {@ApiResponse(responseCode = "200")}, description = "Retrieve issue by URI")
    @GetMapping(value = "/findByUri")
    ResponseEntity<EntityModel<IssueModel>> getIssueByUri(@RequestParam String uri) {
        return ResponseEntity.ok(new EntityModel<>(new IssueModel(this.issueService.getIssueByURI(uri))));
    }


    @Operation(operationId = "getAllIssues", responses = {@ApiResponse(responseCode = "200")}, description = "Retrieve all issues")
    @GetMapping(value = "")
    CollectionModel<EntityModel<IssueModel>> getAll() {
        List<EntityModel<IssueModel>> issues = this.issueService.getAllIssues()
                .stream()
                .map(issue -> new EntityModel<>(new IssueModel(issue)))
                .sorted((i1, i2) -> Integer.compare(i2.getContent().getRating(), i1.getContent().getRating()))
                .collect(Collectors.toList());
        return CollectionModel.of(issues);
    }

    @Operation(operationId = "getIssueById", responses = {@ApiResponse(responseCode = "200")}, description = "Retrieve issue by id")
    @GetMapping(value = "/{issueId}")
    ResponseEntity<EntityModel<IssueModel>> getIssueById(@PathVariable UUID issueId) {
        return ResponseEntity.ok(new EntityModel<>(new IssueModel(this.issueService.getIssueById(issueId))));
    }

    /**
     * CREATE Methods
     */
    @Operation(operationId = "createIssue", responses = {@ApiResponse(responseCode = "201")}, description = "Create an issue")
    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<IssueModel>> newIssue(@RequestBody IssueModelRequest issueModelRequest, Principal principal) {
        return ResponseEntity.ok(new EntityModel<>(new IssueModel(this.issueService.createIssue(issueModelRequest, UUID.fromString(principal.getName())))));
    }

    @Operation(operationId = "createIssueComment", responses = {@ApiResponse(responseCode = "201")}, description = "Create an issue comment")
    @PostMapping(value = "/{issueId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<IssueModel>> newIssueComment(@PathVariable UUID issueId, Principal principal, @RequestBody CommentModel commentModel) {
        return ResponseEntity.ok(new EntityModel<>(new IssueModel(this.issueService.createComment(issueId, UUID.fromString(principal.getName()), commentModel))));
    }

    @Operation(operationId = "createIssueEvidence", responses = {@ApiResponse(responseCode = "201")}, description = "Create an issue evidence")
    @PostMapping(value = "/{issueId}/evidences")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<IssueModel>> newIssueEvidence(@PathVariable UUID issueId, Principal principal, @RequestBody EvidenceModel evidenceModel) {
        return ResponseEntity.ok(new EntityModel<>(new IssueModel(this.issueService.createEvidence(issueId, UUID.fromString(principal.getName()), evidenceModel))));
    }

    /**
     * UPDATE Methods
     */
    @Operation(operationId = "updateIssue", responses = {@ApiResponse(responseCode = "200")}, description = "Update an issue")
    @PutMapping(value = "/{issueId}")
    ResponseEntity<EntityModel<IssueModel>> putIssue(@PathVariable UUID issueId, Principal principal, @RequestBody IssueModelRequest issueModelRequest) {
        return ResponseEntity.ok(new EntityModel<>(new IssueModel(this.issueService.updateIssue(issueId, UUID.fromString(principal.getName()), issueModelRequest))));
    }

    @Operation(operationId = "updateIssueRatings", responses = {@ApiResponse(responseCode = "200")}, description = "Update an issue ratings")
    @PutMapping(value = "/{issueId}/ratings")
    ResponseEntity<EntityModel<IssueModel>> putIssueRating(@PathVariable UUID issueId, Principal principal, @RequestBody RatingModelRequest ratingModelRequest) {
        return ResponseEntity.ok(new EntityModel<>(new IssueModel(this.issueService.updateIssueRating(issueId, UUID.fromString(principal.getName()), ratingModelRequest))));
    }

    @Operation(operationId = "updateIssueAuthors", responses = {@ApiResponse(responseCode = "200")}, description = "Update an issue authors")
    @PutMapping(value = "{issueId}/authors")
    ResponseEntity<EntityModel<IssueModel>> putIssueAuthor(@PathVariable UUID issueId, @RequestBody AuthorModelRequest authorModelRequest) {
        return ResponseEntity.ok(new EntityModel<>(new IssueModel(this.issueService.saveIssueAuthor(issueId, authorModelRequest))));
    }

    @Operation(operationId = "updateIssue", responses = {@ApiResponse(responseCode = "200")}, description = "Update an issue comment")
    @PutMapping(value = "/{issueId}/comments/{issueCommentId}")
    ResponseEntity<EntityModel<IssueModel>> putIssueComment(@PathVariable UUID issueId, @PathVariable UUID issueCommentId, Principal principal, @RequestBody CommentModel commentModel) {
        return ResponseEntity.ok(new EntityModel<>(new IssueModel(this.issueService.updateComment(issueId, issueCommentId, UUID.fromString(principal.getName()), commentModel))));
    }

    @Operation(operationId = "updateIssue", responses = {@ApiResponse(responseCode = "200")}, description = "Update an issue comment rating")
    @PutMapping(value = "/{issueId}/comments/{issueCommentId}/ratings")
    ResponseEntity<EntityModel<IssueModel>> putIssueCommentRating(@PathVariable UUID issueId, @PathVariable UUID issueCommentId, Principal principal, @RequestBody RatingModelRequest ratingModelRequest) {
        return ResponseEntity.ok(new EntityModel<>(new IssueModel(this.issueService.updateIssueCommentRating(issueId, issueCommentId, UUID.fromString(principal.getName()), ratingModelRequest))));
    }

    @Operation(operationId = "updateIssue", responses = {@ApiResponse(responseCode = "200")}, description = "Update an issue evidence")
    @PutMapping(value = "/{issueId}/evidences/{issueEvidenceId}")
    ResponseEntity<EntityModel<IssueModel>> putIssueEvidence(@PathVariable UUID issueId, @PathVariable UUID issueEvidenceId, Principal principal, @RequestBody EvidenceModel evidenceModel) {
        return ResponseEntity.ok(new EntityModel<>(new IssueModel(this.issueService.updateEvidence(issueId, issueEvidenceId, UUID.fromString(principal.getName()), evidenceModel))));
    }

    @Operation(operationId = "updateIssueEvidenceRating", responses = {@ApiResponse(responseCode = "200")}, description = "Update an issue evidence rating")
    @PutMapping(value = "/{issueId}/evidences/{issueEvidenceId}/ratings")
    ResponseEntity<EntityModel<IssueModel>> putIssueEvidenceRating(@PathVariable UUID issueId, @PathVariable UUID issueEvidenceId, Principal principal, @RequestBody RatingModelRequest ratingModelRequest) {
        return ResponseEntity.ok(new EntityModel<>(new IssueModel(this.issueService.updateIssueEvidenceRating(issueId, issueEvidenceId, UUID.fromString(principal.getName()), ratingModelRequest))));
    }

    /**
     * DELETE Methods
     */
    @Operation(operationId = "deleteIssue", responses = {@ApiResponse(responseCode = "200")}, description = "Delete an issue")
    @DeleteMapping(value = "/{issueId}")
    ResponseEntity<?> deleteIssue(@PathVariable UUID issueId) {
        this.issueService.deleteIssue(issueId);
        return ResponseEntity.noContent().build();
    }

    @Operation(operationId = "deleteIssueAuthor", responses = {@ApiResponse(responseCode = "200")}, description = "Delete an issue")
    @DeleteMapping(value = "{issueId}/authors/{userId}")
    ResponseEntity<EntityModel<IssueModel>> deleteIssueAuthor(@PathVariable UUID issueId, @PathVariable UUID userId) {
        return ResponseEntity.ok(new EntityModel<>(new IssueModel(this.issueService.deleteIssueAuthor(issueId, userId))));
    }

    @Operation(operationId = "deleteIssueComment", responses = {@ApiResponse(responseCode = "200")}, description = "Delete an issue comment")
    @DeleteMapping(value = "/{issueId}/comments/{issueCommentId}")
    ResponseEntity<EntityModel<IssueModel>> deleteComment(@PathVariable UUID issueId, @PathVariable UUID issueCommentId, Principal principal) {
        return ResponseEntity.ok(new EntityModel<>(new IssueModel(this.issueService.deleteComment(issueId, issueCommentId, UUID.fromString(principal.getName())))));
    }

    @Operation(operationId = "deleteIssueEvidence", responses = {@ApiResponse(responseCode = "200")}, description = "Delete an issue evidence")
    @DeleteMapping(value = "/{issueId}/evidences/{issueEvidenceId}")
    ResponseEntity<EntityModel<IssueModel>> deleteEvidence(@PathVariable UUID issueId, @PathVariable UUID issueEvidenceId, Principal principal) {
        return ResponseEntity.ok(new EntityModel<>(new IssueModel(this.issueService.deleteEvidence(issueId, issueEvidenceId, UUID.fromString(principal.getName())))));
    }
}