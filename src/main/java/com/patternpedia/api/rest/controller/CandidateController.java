package com.patternpedia.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.candidate.Candidate;
import com.patternpedia.api.entities.candidate.CandidateComment;
import com.patternpedia.api.entities.issue.Issue;
import com.patternpedia.api.rest.model.CandidateModel;
import com.patternpedia.api.rest.model.PatternLanguageModel;
import com.patternpedia.api.rest.model.PatternModel;
import com.patternpedia.api.service.CandidateService;
import com.patternpedia.api.service.PatternLanguageService;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.*;

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
                .map(candidate -> new EntityModel<>(CandidateModel.from(candidate)))
//                        getPatternViewLinks(patternView)))
                .collect(Collectors.toList());

        return new CollectionModel<>(candidates);
    }

    @Operation(operationId = "getCandidateById", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Retrieve a single candidate by id")
    @GetMapping(value = "/{candidateId}")
    @PreAuthorize(value = "#oauth2.hasScope('read')")
    Candidate getCandidateById(@PathVariable UUID candidateId) {
        return this.candidateService.getCandidateById(candidateId);
    }

    @Operation(operationId = "getCandidateByURI", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Retrieve a single candidate by URI")
    @GetMapping(value = "/?uri={candidateUri}")
    Candidate getCandidateByUri(@PathVariable String candidateUri) {
        return this.candidateService.getCandidateByURI(candidateUri);
    }

    /**
     * CREATE Methods
     */
    @Operation(operationId = "createCandidate", responses = {@ApiResponse(responseCode = "201")}, description = "Create a candidate")
    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.CREATED)
    Candidate newCandidate(@RequestBody CandidateModel candidate) {
        return this.candidateService.createCandidate(candidate);
    }

    @Operation(operationId = "createCandidateComment", responses = {@ApiResponse(responseCode = "201")}, description = "Create a candidate comment")
    @PostMapping(value = "/{candidateId}/comments/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    Candidate newCandidateComment(@PathVariable UUID candidateId, @PathVariable UUID userId, @RequestBody CandidateComment candidateComment) {
        return this.candidateService.createComment(candidateId, userId, candidateComment);
    }

    /**
     * UPDATE Methods
     */
    @Operation(operationId = "updateCandidate", responses = {@ApiResponse(responseCode = "200")}, description = "Update a candidate")
    @PutMapping(value = "/{candidateId}")
    Candidate putCandidate(@PathVariable UUID candidateId, @RequestBody Candidate candidate) {
        logger.info(candidate.toString());
        return this.candidateService.updateCandidate(candidate);
    }

    @Operation(operationId = "updateCandidateRating", responses = {@ApiResponse(responseCode = "200")}, description = "Update rating of a candidate")
    @PutMapping(value = "/{candidateId}/users/{userId}/rating/{rating}")
    Candidate putCandidateRating(@PathVariable UUID candidateId, @PathVariable UUID userId, @PathVariable String rating) {
        return this.candidateService.userRating(candidateId, userId, rating);
    }

    @Operation(operationId = "updateCandidateCommentRating", responses = {@ApiResponse(responseCode = "200")}, description = "Update rating of a candidate comment")
    @PutMapping(value = "/comments/{candidateCommentId}/users/{userId}/rating/{rating}")
    Candidate putCandidateCommentRating(@PathVariable UUID candidateCommentId, @PathVariable UUID userId, @PathVariable String rating) {
        return this.candidateService.commentUserRating(candidateCommentId, userId, rating);
    }

    /**
     * DELETE Methods
     */
    @Operation(operationId = "deleteCandidateById", responses = {@ApiResponse(responseCode = "204")}, description = "Delete candidate by id")
    @DeleteMapping(value = "/{candidateId}")
//    @PreAuthorize(value = "#oauth2.hasScope('de')")
    ResponseEntity<?> deleteCandidate(@PathVariable UUID candidateId) {
        this.candidateService.deleteCandidate(candidateId);
        return ResponseEntity.noContent().build();
    }
}
