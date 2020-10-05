package com.patternpedia.api.service;

import com.patternpedia.api.entities.candidate.Candidate;
import com.patternpedia.api.entities.candidate.comment.CandidateComment;
import com.patternpedia.api.entities.candidate.evidence.CandidateEvidence;
import com.patternpedia.api.rest.model.candidate.CandidateModelRequest;
import com.patternpedia.api.rest.model.shared.CommentModel;
import com.patternpedia.api.rest.model.shared.EvidenceModel;
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

    void deleteCandidate(UUID candidateId, UUID userId);

    /** Comment */
    CandidateComment createComment(UUID candidateId, UUID userId, CommentModel commentModel);

    CandidateComment getCommentById(UUID candidateCommentId);

    CandidateComment updateComment(UUID candidateId, UUID commentId, UUID userId, CommentModel commentModel);

    ResponseEntity<?> deleteComment(UUID candidateId, UUID commentId, UUID userId);

    /** Evidence */
    CandidateEvidence createEvidence(UUID candidateId, UUID userId, EvidenceModel evidenceModel);

    CandidateEvidence getEvidenceById(UUID evidenceId);

    CandidateEvidence updateEvidence(UUID candidateId, UUID evidenceId, UUID userId, EvidenceModel evidenceModel);

    ResponseEntity<?> deleteEvidence(UUID candidateId, UUID evidenceId, UUID userId);
}
