package io.github.patternatlas.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.patternatlas.api.config.Authority;
import io.github.patternatlas.api.rest.model.candidate.CandidateModel;
import io.github.patternatlas.api.rest.model.candidate.CandidateModelRequest;
import io.github.patternatlas.api.rest.model.issue.IssueModel;
import io.github.patternatlas.api.rest.model.shared.*;
import io.github.patternatlas.api.service.CandidateService;
import io.github.patternatlas.api.service.PatternLanguageService;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

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
@RequestMapping(value = "/candidates", produces = "application/hal+json")
public class CandidateController {

    Logger logger = LoggerFactory.getLogger(CandidateController.class);

    private CandidateService candidateService;
    private PatternLanguageService patternLanguageService;
    private ObjectMapper objectMapper;

    public CandidateController(
            CandidateService candidateService,
            PatternLanguageService patternLanguageService,
            ObjectMapper objectMapper
    ) {
        this.candidateService = candidateService;
        this.patternLanguageService = patternLanguageService;
        this.objectMapper = objectMapper;
    }

    /**
     * GET Methods
     */
    @Operation(operationId = "getAllCandiates", responses = {@ApiResponse(responseCode = "200")}, description = "Retrieve all candidates")
    @GetMapping(value = "")
    CollectionModel<EntityModel<CandidateModel>> all() {

        List<EntityModel<CandidateModel>> candidates = this.candidateService.getAllCandidates()
                .stream()
                .map(candidate -> new EntityModel<>(new CandidateModel(candidate)))
                .collect(Collectors.toList());

        return new CollectionModel<>(candidates);
    }

    @Operation(operationId = "getCandidateById", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404", content = @Content)}, description = "Retrieve a single candidate by id")
    @GetMapping(value = "/{candidateId}")
    ResponseEntity<EntityModel<CandidateModel>> getCandidateById(@PathVariable UUID candidateId) {
        return ResponseEntity.ok(new EntityModel<>(new CandidateModel(this.candidateService.getCandidateById(candidateId))));
    }

    @Operation(operationId = "getCandidateByURI", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404", content = @Content)}, description = "Retrieve a single candidate by URI")
    @GetMapping(value = "/?uri={candidateUri}")
    ResponseEntity<EntityModel<CandidateModel>> getCandidateByUri(@PathVariable String candidateUri) {
        return ResponseEntity.ok(new EntityModel<>(new CandidateModel(this.candidateService.getCandidateByURI(candidateUri))));
    }

    /**
     * CREATE Methods
     */
    @Operation(operationId = "createCandidate", responses = {@ApiResponse(responseCode = "201")}, description = "Create a candidate")
    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<CandidateModel>> newCandidate(@RequestBody CandidateModelRequest candidateModelRequest, @AuthenticationPrincipal Principal principal) {
        return ResponseEntity.ok(new EntityModel<>(new CandidateModel(this.candidateService.createCandidate(candidateModelRequest, UUID.fromString(principal.getName())))));
    }

    @Operation(operationId = "createCandidateComment", responses = {@ApiResponse(responseCode = "201")}, description = "Create a candidate comment")
    @PostMapping(value = "/{candidateId}/comments")
    @PreAuthorize(value = Authority.COMMENT)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<CandidateModel>> newCandidateComment(@PathVariable UUID candidateId, @AuthenticationPrincipal Principal principal, @RequestBody CommentModel commentModel) {
        return ResponseEntity.ok(new EntityModel<>(new CandidateModel(this.candidateService.createComment(candidateId, UUID.fromString(principal.getName()), commentModel))));
    }

    @Operation(operationId = "createCandidateEvidence", responses = {@ApiResponse(responseCode = "201")}, description = "Create a candidate evidence")
    @PostMapping(value = "/{candidateId}/evidences")
    @PreAuthorize(value = Authority.EVIDENCE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<CandidateModel>> newIssueEvidence(@PathVariable UUID candidateId, @AuthenticationPrincipal Principal principal, @RequestBody EvidenceModel evidenceModel) {
        return ResponseEntity.ok(new EntityModel<>(new CandidateModel(this.candidateService.createEvidence(candidateId, UUID.fromString(principal.getName()), evidenceModel))));
    }

    /**
     * UPDATE Methods
     */
    @Operation(operationId = "updateCandidate", responses = {@ApiResponse(responseCode = "200")}, description = "Update a candidate")
    @PutMapping(value = "/{candidateId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<EntityModel<CandidateModel>> putCandidate(@PathVariable UUID candidateId, @AuthenticationPrincipal Principal principal, @RequestBody CandidateModelRequest candidateModelRequest) {
        return ResponseEntity.ok(new EntityModel<>(new CandidateModel(this.candidateService.updateCandidate(candidateId, UUID.fromString(principal.getName()), candidateModelRequest))));
    }

    @Operation(operationId = "updateCandidateRating", responses = {@ApiResponse(responseCode = "200")}, description = "Update a candidate rating")
    @PutMapping(value = "/{candidateId}/ratings")
    ResponseEntity<EntityModel<CandidateModel>> putCandidateRating(@PathVariable UUID candidateId, @AuthenticationPrincipal Principal principal, @RequestBody RatingModelMultiRequest ratingModelMultiRequest) {
        return ResponseEntity.ok(new EntityModel<>(new CandidateModel(this.candidateService.updateCandidateRating(candidateId, UUID.fromString(principal.getName()), ratingModelMultiRequest))));
    }

    @Operation(operationId = "updateCandidateAuthors", responses = {@ApiResponse(responseCode = "200")}, description = "Update a candidate authors")
    @PutMapping(value = "{candidateId}/authors")
    ResponseEntity<EntityModel<CandidateModel>> putCandidateAuthor(@PathVariable UUID candidateId, @RequestBody AuthorModelRequest authorModelRequest) {
        return ResponseEntity.ok(new EntityModel<>(new CandidateModel(this.candidateService.saveCandidateAuthor(candidateId, authorModelRequest))));
    }

    @Operation(operationId = "updateCandidateComment", responses = {@ApiResponse(responseCode = "200")}, description = "Update a candidate comment")
    @PutMapping(value = "/{candidateId}/comments/{candidateCommentId}")
    @PreAuthorize(value = Authority.COMMENT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<EntityModel<CandidateModel>> putCandidateComment(@PathVariable UUID candidateId, @PathVariable UUID candidateCommentId, @AuthenticationPrincipal Principal principal, @RequestBody CommentModel commentModel) {
        return ResponseEntity.ok(new EntityModel<>(new CandidateModel(this.candidateService.updateComment(candidateId, candidateCommentId, UUID.fromString(principal.getName()), commentModel))));
    }

    @Operation(operationId = "updateCandidateCommentRating", responses = {@ApiResponse(responseCode = "200")}, description = "Update a candidate comment rating")
    @PutMapping(value = "/{candidateId}/comments/{candidateCommentId}/ratings")
    ResponseEntity<EntityModel<CandidateModel>> putCandidateCommentRating(@PathVariable UUID candidateId, @PathVariable UUID candidateCommentId, @AuthenticationPrincipal Principal principal, @RequestBody RatingModelRequest ratingModelRequest) {
        return ResponseEntity.ok(new EntityModel<>(new CandidateModel(this.candidateService.updateCandidateCommentRating(candidateId, candidateCommentId, UUID.fromString(principal.getName()), ratingModelRequest))));
    }

    @Operation(operationId = "updateCandidateEvidence", responses = {@ApiResponse(responseCode = "200")}, description = "Update a candidate evidence")
    @PutMapping(value = "/{candidateId}/evidences/{candidateEvidenceId}")
    @PreAuthorize(value = Authority.EVIDENCE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<EntityModel<CandidateModel>> putIssueEvidenceRating(@PathVariable UUID candidateId, @PathVariable UUID candidateEvidenceId, @AuthenticationPrincipal Principal principal, @RequestBody EvidenceModel evidenceModel) {
        return ResponseEntity.ok(new EntityModel<>(new CandidateModel(this.candidateService.updateEvidence(candidateId, candidateEvidenceId, UUID.fromString(principal.getName()), evidenceModel))));
    }

    @Operation(operationId = "updateCandidateEvidenceRatings", responses = {@ApiResponse(responseCode = "200")}, description = "Update a candidate evidence rating")
    @PutMapping(value = "/{candidateId}/evidences/{candidateEvidenceId}/ratings")
    ResponseEntity<EntityModel<CandidateModel>> putCandidateEvidenceRating(@PathVariable UUID candidateId, @PathVariable UUID candidateEvidenceId, @AuthenticationPrincipal Principal principal, @RequestBody RatingModelRequest ratingModelRequest) {
        return ResponseEntity.ok(new EntityModel<>(new CandidateModel(this.candidateService.updateCandidateEvidenceRating(candidateId, candidateEvidenceId, UUID.fromString(principal.getName()), ratingModelRequest))));
    }

    /**
     * DELETE Methods
     */
    @Operation(operationId = "deleteCandidateById", responses = {@ApiResponse(responseCode = "204")}, description = "Delete candidate by id")
    @DeleteMapping(value = "/{candidateId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<?> deleteCandidate(@PathVariable UUID candidateId, @AuthenticationPrincipal Principal principal) {
        this.candidateService.deleteCandidate(candidateId, UUID.fromString(principal.getName()));
        return ResponseEntity.noContent().build();
    }

    @Operation(operationId = "deleteCandidateAuthor", responses = {@ApiResponse(responseCode = "204")}, description = "Delete candidate author")
    @DeleteMapping(value = "{candidateId}/authors/{userId}")
    ResponseEntity<EntityModel<CandidateModel>> deleteCandidateAuthor(@PathVariable UUID candidateId, @PathVariable UUID userId) {
        return ResponseEntity.ok(new EntityModel<>(new CandidateModel(this.candidateService.deleteCandidateAuthor(candidateId, userId))));
    }

    @Operation(operationId = "deleteCandidateComment", responses = {@ApiResponse(responseCode = "204")}, description = "Delete candidate comment")
    @DeleteMapping(value = "/{candidateId}/comments/{candidateCommentId}")
    @PreAuthorize(value = Authority.COMMENT)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<EntityModel<CandidateModel>> deleteComment(@PathVariable UUID candidateId, @PathVariable UUID candidateCommentId, @AuthenticationPrincipal Principal principal) {
        return ResponseEntity.ok(new EntityModel<>(new CandidateModel(this.candidateService.deleteComment(candidateId, candidateCommentId, UUID.fromString(principal.getName())))));
    }

    @Operation(operationId = "deleteCandidateEvidence", responses = {@ApiResponse(responseCode = "204")}, description = "Delete candidate evidence")
    @DeleteMapping(value = "/{candidateId}/evidences/{candidateEvidenceId}")
    @PreAuthorize(value = Authority.EVIDENCE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<EntityModel<CandidateModel>> deleteEvidence(@PathVariable UUID candidateId, @PathVariable UUID candidateEvidenceId, @AuthenticationPrincipal Principal principal) {
        return ResponseEntity.ok(new EntityModel<>(new CandidateModel(this.candidateService.deleteEvidence(candidateId, candidateEvidenceId, UUID.fromString(principal.getName())))));
    }
}