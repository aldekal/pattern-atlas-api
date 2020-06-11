package com.patternpedia.api.service;

import com.patternpedia.api.entities.candidate.Candidate;
import com.patternpedia.api.entities.candidate.comment.CandidateComment;
import com.patternpedia.api.rest.model.candidate.CandidateModelRequest;
import com.patternpedia.api.rest.model.shared.CommentModel;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CandidateService {
    Candidate saveCandidate(Candidate candidate);
    /** CRUD  */
    Candidate createCandidate(CandidateModelRequest candidateModelRequest, UUID userId);

    List<Candidate> getAllCandidates();

    Candidate getCandidateById(UUID candidateId);

    Candidate getCandidateByURI(String uri);

    Candidate updateCandidate(UUID candidateId, UUID userId, CandidateModelRequest candidateModelRequest);

    void deleteCandidate(UUID candidateId);

    /** Comment */
    CandidateComment createComment(UUID candidateId, UUID userId, CommentModel commentModel);

    CandidateComment getCommentById(UUID candidateCommentId);

    CandidateComment updateComment(UUID candidateId, UUID commentId, UUID userId, CommentModel commentModel);

    ResponseEntity<?> deleteComment(UUID candidateId, UUID commentId, UUID userId);
}
