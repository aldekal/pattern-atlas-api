package com.patternpedia.api.service;

import com.patternpedia.api.entities.candidate.Candidate;
import com.patternpedia.api.entities.candidate.CandidateRating;
import com.patternpedia.api.entities.candidate.comment.CandidateCommentRating;
import com.patternpedia.api.entities.issue.IssueRating;
import com.patternpedia.api.entities.issue.comment.IssueComment;
import com.patternpedia.api.entities.issue.comment.IssueCommentRating;
import com.patternpedia.api.rest.model.shared.RatingModelRequest;

import java.util.Collection;
import java.util.UUID;

public interface RatingService {

    IssueRating updateIssueRating(UUID issueId, UUID userId, RatingModelRequest ratingModelRequest);
    IssueCommentRating updateIssueCommentRating(UUID issueId, UUID commentId, UUID userId, RatingModelRequest ratingModelRequest);

    CandidateRating updateCandidateRating(UUID candidateId, UUID userId, RatingModelRequest ratingModelRequest);
    CandidateCommentRating updateCandidateCommentRating(UUID candidateId, UUID commentId, UUID userId, RatingModelRequest ratingModelRequest);
}
