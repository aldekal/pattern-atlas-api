package com.patternpedia.api.rest.model.shared;

import com.patternpedia.api.entities.candidate.CandidateRating;
import com.patternpedia.api.entities.candidate.comment.CandidateCommentRating;
import com.patternpedia.api.entities.issue.IssueRating;
import com.patternpedia.api.entities.issue.comment.IssueCommentRating;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
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

    public RatingModel(CandidateRating candidateRating) {
        this.rating = candidateRating.getRating();
        this.userId = candidateRating.getUser().getId();
    }

    public RatingModel(CandidateCommentRating candidateCommentRating) {
        this.rating = candidateCommentRating.getRating();
        this.userId = candidateCommentRating.getUser().getId();
    }
}
