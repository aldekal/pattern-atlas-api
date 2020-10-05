package com.patternpedia.api.rest.controller;

import com.patternpedia.api.rest.model.candidate.CandidateModel;
import com.patternpedia.api.rest.model.issue.IssueModel;
import com.patternpedia.api.rest.model.shared.*;
import com.patternpedia.api.service.RatingService;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping(value = "/ratings", produces = "application/hal+json")
public class RatingController {

    private RatingService ratingService;

    public RatingController(
            RatingService ratingService
    ) {
        this.ratingService = ratingService;
    }

    /**
     * UPDATE Methods
     */
    @PutMapping(value = "/issues/{issueId}")
    ResponseEntity<EntityModel<RatingModel>> putIssueRating(@PathVariable UUID issueId, @AuthenticationPrincipal Principal principal, @RequestBody RatingModelRequest ratingModelRequest) {
        return ResponseEntity.ok(new EntityModel<>(new RatingModel(this.ratingService.updateIssueRating(issueId, UUID.fromString(principal.getName()), ratingModelRequest))));
    }

    @PutMapping(value = "/issues/{issueId}/comments/{issueCommentId}")
    ResponseEntity<EntityModel<RatingModel>> putIssueCommentRating(@PathVariable UUID issueId, @PathVariable UUID issueCommentId, @AuthenticationPrincipal Principal principal, @RequestBody RatingModelRequest ratingModelRequest) {
        return ResponseEntity.ok(new EntityModel<>(new RatingModel(this.ratingService.updateIssueCommentRating(issueId, issueCommentId, UUID.fromString(principal.getName()), ratingModelRequest))));
    }

    @PutMapping(value = "/issues/{issueId}/evidences/{issueEvidenceId}")
    ResponseEntity<EntityModel<RatingModel>> putIssueEvidenceRating(@PathVariable UUID issueId, @PathVariable UUID issueEvidenceId, @AuthenticationPrincipal Principal principal, @RequestBody RatingModelMultiRequest ratingModelMultiRequest) {
        return ResponseEntity.ok(new EntityModel<>(new RatingModel(this.ratingService.updateIssueEvidenceRating(issueId, issueEvidenceId, UUID.fromString(principal.getName()), ratingModelMultiRequest))));
    }

    @PutMapping(value = "/candidates/{candidateId}")
    ResponseEntity<EntityModel<RatingModel>> putCandidateRating(@PathVariable UUID candidateId, @AuthenticationPrincipal Principal principal, @RequestBody RatingModelMultiRequest ratingModelMultiRequest) {
        return ResponseEntity.ok(new EntityModel<>(this.ratingService.updateCandidateRating(candidateId, UUID.fromString(principal.getName()), ratingModelMultiRequest)));
    }

    @PutMapping(value = "/candidates/{candidateId}/comments/{candidateCommentId}")
    ResponseEntity<EntityModel<RatingModel>> putCandidateCommentRating(@PathVariable UUID candidateId, @PathVariable UUID candidateCommentId, @AuthenticationPrincipal Principal principal, @RequestBody RatingModelRequest ratingModelRequest) {
        return ResponseEntity.ok(new EntityModel<>(new RatingModel(this.ratingService.updateCandidateCommentRating(candidateId, candidateCommentId, UUID.fromString(principal.getName()), ratingModelRequest))));
    }

    @PutMapping(value = "/candidates/{candidateId}/evidences/{candidateEvidenceId}")
    ResponseEntity<EntityModel<RatingModel>> putCandidateEvidenceRating(@PathVariable UUID candidateId, @PathVariable UUID candidateEvidenceId, @AuthenticationPrincipal Principal principal, @RequestBody RatingModelMultiRequest ratingModelMultiRequest) {
        return ResponseEntity.ok(new EntityModel<>(new RatingModel(this.ratingService.updateCandidateEvidenceRating(candidateId, candidateEvidenceId, UUID.fromString(principal.getName()), ratingModelMultiRequest))));
    }
}
