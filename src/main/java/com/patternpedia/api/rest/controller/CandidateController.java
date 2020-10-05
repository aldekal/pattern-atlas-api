package com.patternpedia.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patternpedia.api.config.Authority;
import com.patternpedia.api.rest.model.candidate.CandidateModel;
import com.patternpedia.api.rest.model.candidate.CandidateModelRequest;
import com.patternpedia.api.rest.model.shared.CommentModel;
import com.patternpedia.api.rest.model.shared.EvidenceModel;
import com.patternpedia.api.service.CandidateService;
import com.patternpedia.api.service.PatternLanguageService;
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
    @GetMapping(value = "")
    CollectionModel<EntityModel<CandidateModel>> all() {
        List<EntityModel<CandidateModel>> candidates = this.candidateService.getAllCandidates()
                .stream()
                .map(candidate -> new EntityModel<>(new CandidateModel(candidate)))
                .collect(Collectors.toList());
        return new CollectionModel<>(candidates);
    }

    @GetMapping(value = "/{candidateId}")
    ResponseEntity<EntityModel<CandidateModel>> getCandidateById(@PathVariable UUID candidateId) {
        return ResponseEntity.ok(new EntityModel<>(new CandidateModel(this.candidateService.getCandidateById(candidateId))));
    }

    @GetMapping(value = "/?uri={candidateUri}")
    ResponseEntity<EntityModel<CandidateModel>> getCandidateByUri(@PathVariable String candidateUri) {
        return ResponseEntity.ok(new EntityModel<>(new CandidateModel(this.candidateService.getCandidateByURI(candidateUri))));
    }

    /**
     * CREATE Methods
     */
    @PostMapping(value = "")
//    @PreAuthorize(value = Authority.PATTERN_CANDIDATE_CREATE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<CandidateModel>> newCandidate(@RequestBody CandidateModelRequest candidateModelRequest, @AuthenticationPrincipal Principal principal) {
        return ResponseEntity.ok(new EntityModel<>(new CandidateModel(this.candidateService.createCandidate(candidateModelRequest, UUID.fromString(principal.getName())))));
    }

    @PostMapping(value = "/{candidateId}/comments")
    @PreAuthorize(value = Authority.COMMENT)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<CommentModel>> newCandidateComment(@PathVariable UUID candidateId, @AuthenticationPrincipal Principal principal, @RequestBody CommentModel commentModel) {
        return ResponseEntity.ok(new EntityModel<>(new CommentModel(this.candidateService.createComment(candidateId, UUID.fromString(principal.getName()), commentModel))));
    }

    @PostMapping(value = "/{candidateId}/evidences")
    @PreAuthorize(value = Authority.EVIDENCE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<EvidenceModel>> newIssueEvidence(@PathVariable UUID candidateId, @AuthenticationPrincipal Principal principal, @RequestBody EvidenceModel evidenceModel) {
        return ResponseEntity.ok(new EntityModel<>(new EvidenceModel(this.candidateService.createEvidence(candidateId, UUID.fromString(principal.getName()), evidenceModel))));
    }

    /**
     * UPDATE Methods
     */
    @PutMapping(value = "/{candidateId}")
//    @PreAuthorize(value = Authority.PATTERN_CANDIDATE_EDIT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<EntityModel<CandidateModel>> putCandidate(@PathVariable UUID candidateId, @AuthenticationPrincipal Principal principal, @RequestBody CandidateModelRequest candidateModelRequest) {
        return ResponseEntity.ok(new EntityModel<>(new CandidateModel(this.candidateService.updateCandidate(candidateId, UUID.fromString(principal.getName()), candidateModelRequest))));
    }

    @PutMapping(value = "/{candidateId}/comments/{candidateCommentId}")
    @PreAuthorize(value = Authority.COMMENT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<EntityModel<CommentModel>> putCandidateCommentRating(@PathVariable UUID candidateId, @PathVariable UUID candidateCommentId, @AuthenticationPrincipal Principal principal, @RequestBody CommentModel commentModel) {
        return ResponseEntity.ok(new EntityModel<>(new CommentModel(this.candidateService.updateComment(candidateId, candidateCommentId, UUID.fromString(principal.getName()), commentModel))));
    }


    @PutMapping(value = "/{candidateId}/evidences/{candidateEvidenceId}")
    @PreAuthorize(value = Authority.EVIDENCE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<EntityModel<EvidenceModel>> putIssueEvidenceRating(@PathVariable UUID candidateId, @PathVariable UUID candidateEvidenceId, @AuthenticationPrincipal Principal principal, @RequestBody EvidenceModel evidenceModel) {
        return ResponseEntity.ok(new EntityModel<>(new EvidenceModel(this.candidateService.updateEvidence(candidateId, candidateEvidenceId, UUID.fromString(principal.getName()), evidenceModel))));
    }

    /**
     * DELETE Methods
     */
    @DeleteMapping(value = "/{candidateId}")
//    @PreAuthorize(value = Authority.PATTERN_CANDIDATE_DELETE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<?> deleteCandidate(@PathVariable UUID candidateId, @AuthenticationPrincipal Principal principal) {
        this.candidateService.deleteCandidate(candidateId, UUID.fromString(principal.getName()));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{candidateId}/comments/{candidateCommentId}")
    @PreAuthorize(value = Authority.COMMENT)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<?> deleteComment(@PathVariable UUID candidateId, @PathVariable UUID candidateCommentId, @AuthenticationPrincipal Principal principal) {
        return this.candidateService.deleteComment(candidateId, candidateCommentId, UUID.fromString(principal.getName()));
    }

    @DeleteMapping(value = "/{issueId}/evidences/{issueEvidenceId}")
    @PreAuthorize(value = Authority.EVIDENCE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<?> deleteEvidence(@PathVariable UUID candidateId, @PathVariable UUID candidateEvidenceId, @AuthenticationPrincipal Principal principal) {
        return this.candidateService.deleteEvidence(candidateId, candidateEvidenceId, UUID.fromString(principal.getName()));
    }
}
