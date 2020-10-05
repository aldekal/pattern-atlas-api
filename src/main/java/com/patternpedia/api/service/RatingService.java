package com.patternpedia.api.service;

import com.patternpedia.api.entities.candidate.CandidateRating;
import com.patternpedia.api.entities.candidate.comment.CandidateCommentRating;
import com.patternpedia.api.entities.candidate.evidence.CandidateEvidenceRating;
import com.patternpedia.api.entities.issue.IssueRating;
import com.patternpedia.api.entities.issue.comment.IssueCommentRating;
import com.patternpedia.api.entities.issue.evidence.IssueEvidenceRating;
import com.patternpedia.api.rest.model.shared.RatingModel;
import com.patternpedia.api.rest.model.shared.RatingModelMultiRequest;
import com.patternpedia.api.rest.model.shared.RatingModelRequest;

import java.util.UUID;

public interface RatingService {

    IssueRating updateIssueRating(UUID issueId, UUID userId, RatingModelRequest ratingModelRequest);
    IssueCommentRating updateIssueCommentRating(UUID issueId, UUID commentId, UUID userId, RatingModelRequest ratingModelRequest);
    IssueEvidenceRating updateIssueEvidenceRating(UUID issueId, UUID evidenceID, UUID userId, RatingModelRequest ratingModelRequest);

    RatingModel updateCandidateRating(UUID candidateId, UUID userId, RatingModelMultiRequest ratingModelMultiRequest);
    CandidateCommentRating updateCandidateCommentRating(UUID candidateId, UUID commentId, UUID userId, RatingModelRequest ratingModelRequest);
    CandidateEvidenceRating updateCandidateEvidenceRating(UUID candidateId, UUID evidenceID, UUID userId, RatingModelRequest ratingModelRequest);
}
