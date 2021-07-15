package io.github.patternatlas.api.service;

import io.github.patternatlas.api.entities.candidate.Candidate;
import io.github.patternatlas.api.entities.candidate.author.CandidateAuthor;
import io.github.patternatlas.api.entities.candidate.comment.CandidateComment;
import io.github.patternatlas.api.entities.candidate.comment.CandidateCommentRating;
import io.github.patternatlas.api.entities.candidate.evidence.CandidateEvidence;
import io.github.patternatlas.api.entities.candidate.evidence.CandidateEvidenceRating;
import io.github.patternatlas.api.rest.model.candidate.CandidateModelRequest;
import io.github.patternatlas.api.rest.model.shared.*;

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

    Candidate updateCandidateRating(UUID candidateId, UUID userId, RatingModelMultiRequest ratingModelMultiRequest);

    void deleteCandidate(UUID candidateId, UUID userId);

    /** Author */
    Candidate saveCandidateAuthor(UUID candidateId, AuthorModelRequest authorModelRequest);

    Candidate deleteCandidateAuthor(UUID candidateId, UUID userId);

    /** Comment */
    Candidate createComment(UUID candidateId, UUID userId, CommentModel commentModel);

    CandidateComment getCommentById(UUID candidateCommentId);

    Candidate updateComment(UUID candidateId, UUID commentId, UUID userId, CommentModel commentModel);

    Candidate updateCandidateCommentRating(UUID candidateId, UUID commentId, UUID userId, RatingModelRequest ratingModelRequest);

    Candidate deleteComment(UUID candidateId, UUID commentId, UUID userId);

    /** Evidence */
    Candidate createEvidence(UUID candidateId, UUID userId, EvidenceModel evidenceModel);

    CandidateEvidence getEvidenceById(UUID evidenceId);

    Candidate updateEvidence(UUID candidateId, UUID evidenceId, UUID userId, EvidenceModel evidenceModel);

    Candidate updateCandidateEvidenceRating(UUID candidateId, UUID evidenceID, UUID userId, RatingModelRequest ratingModelRequest);

    Candidate deleteEvidence(UUID candidateId, UUID evidenceId, UUID userId);
}