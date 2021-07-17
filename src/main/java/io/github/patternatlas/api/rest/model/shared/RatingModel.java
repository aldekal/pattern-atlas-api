package io.github.patternatlas.api.rest.model.shared;

import io.github.patternatlas.api.entities.candidate.CandidateRating;
import io.github.patternatlas.api.entities.candidate.comment.CandidateCommentRating;
import io.github.patternatlas.api.entities.candidate.evidence.CandidateEvidenceRating;
import io.github.patternatlas.api.entities.issue.IssueRating;
import io.github.patternatlas.api.entities.issue.comment.IssueCommentRating;
import io.github.patternatlas.api.entities.issue.evidence.IssueEvidenceRating;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class RatingModel {

    private int rating;
    private UUID userId;

    public RatingModel(IssueRating issueRating) {
        this.rating = issueRating.getRating();
        this.userId = issueRating.getUser().getId();
    }

    public RatingModel(IssueCommentRating issueCommentRating) {
        this.rating = issueCommentRating.getRating();
        this.userId = issueCommentRating.getUser().getId();
    }

    public RatingModel(IssueEvidenceRating issueEvidenceRating) {
        this.rating = issueEvidenceRating.getRating();
        this.userId = issueEvidenceRating.getUser().getId();
    }

    public RatingModel(CandidateRating candidateRating, int rating) {
        this.rating = rating;
        this.userId = candidateRating.getUser().getId();
    }

    public RatingModel(CandidateCommentRating candidateCommentRating) {
        this.rating = candidateCommentRating.getRating();
        this.userId = candidateCommentRating.getUser().getId();
    }

    public RatingModel(CandidateEvidenceRating candidateEvidenceRating) {
        this.rating = candidateEvidenceRating.getRating();
        this.userId = candidateEvidenceRating.getUser().getId();
    }
}
