package io.github.patternatlas.api.service;

import java.util.List;
import java.util.UUID;

import io.github.patternatlas.api.entities.candidate.CandidateComment;
import io.github.patternatlas.api.rest.model.CandidateModel;
import io.github.patternatlas.api.entities.candidate.Candidate;

public interface CandidateService {
    /**
     * CRUD
     */
    Candidate createCandidate(CandidateModel candidateModel);

    Candidate updateCandidate(Candidate candidate);

    void deleteCandidate(UUID candidateId);

    Candidate getCandidateById(UUID candidateId);

    Candidate getCandidateByURI(String uri);

    List<Candidate> getAllCandidates();

    /**
     * Voting
     */
    Candidate userRating(UUID candidateId, UUID userId, String rating);

    /**
     * Comment
     */
    Candidate createComment(UUID candidateId, UUID userId, CandidateComment candidateComment);

    CandidateComment getCommentById(UUID candidateCommentId);

    CandidateComment updateComment(CandidateComment candidateComment);

    Candidate commentUserRating(UUID candidateCommentId, UUID userId, String rating);
}
