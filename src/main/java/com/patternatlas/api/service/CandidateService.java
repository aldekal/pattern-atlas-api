package com.patternatlas.api.service;

import com.patternatlas.api.entities.candidate.Candidate;
import com.patternatlas.api.entities.candidate.CandidateComment;
import com.patternatlas.api.rest.model.CandidateModel;

import java.util.List;
import java.util.UUID;

public interface CandidateService {
    /** CRUD  */
    Candidate createCandidate(CandidateModel candidateModel);

    Candidate updateCandidate(Candidate candidate);

    void deleteCandidate(UUID candidateId);

    Candidate getCandidateById(UUID candidateId);

    Candidate getCandidateByURI(String uri);

    List<Candidate> getAllCandidates();

    /** Voting */
    Candidate userRating(UUID candidateId, UUID userId, String rating);

    /** Comment */
    Candidate createComment(UUID candidateId, UUID userId, CandidateComment candidateComment);

    CandidateComment getCommentById(UUID candidateCommentId);

    CandidateComment updateComment(CandidateComment candidateComment);

    Candidate commentUserRating(UUID candidateCommentId, UUID userId, String rating);
}
