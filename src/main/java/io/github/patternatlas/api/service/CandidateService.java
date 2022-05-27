package io.github.patternatlas.api.service;

import java.util.List;
import java.util.UUID;

import io.github.patternatlas.api.entities.candidate.Candidate;
import io.github.patternatlas.api.entities.candidate.comment.CandidateComment;
import io.github.patternatlas.api.entities.candidate.evidence.CandidateEvidence;
import io.github.patternatlas.api.rest.model.candidate.CandidateModelRequest;
import io.github.patternatlas.api.rest.model.shared.AuthorModelRequest;
import io.github.patternatlas.api.rest.model.shared.CommentModel;
import io.github.patternatlas.api.rest.model.shared.EvidenceModel;
import io.github.patternatlas.api.rest.model.shared.RatingModelMultiRequest;
import io.github.patternatlas.api.rest.model.shared.RatingModelRequest;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CandidateService {
    Candidate saveCandidate(Candidate candidate);
    /** CRUD  */
    @PreAuthorize(value = "hasGlobalPermission(@PC.PATTERN_CANDIDATE_CREATE)")
    Candidate createCandidate(CandidateModelRequest candidateModelRequest, UUID userId);

    @PostFilter("hasResourcePermission(filterObject.id, @PC.PATTERN_CANDIDATE_READ)")
    List<Candidate> getAllCandidates();

    @PostFilter("hasResourcePermission(filterObject.id, @PC.PATTERN_CANDIDATE_READ)")
    List<Candidate> getAllCandidatesByLanguageId(UUID languageId);

    @PreAuthorize(value = "hasResourcePermission(#candidateId, @PC.PATTERN_CANDIDATE_READ)")
    Candidate getCandidateById(UUID candidateId);

    @PostAuthorize(value = "hasResourcePermission(returnObject.id, @PC.PATTERN_CANDIDATE_READ)")
    Candidate getCandidateByURI(String uri);

    @PreAuthorize(value = "hasResourcePermission(#candidateId, @PC.PATTERN_CANDIDATE_EDIT)")
    Candidate updateCandidate(UUID candidateId, UUID userId, CandidateModelRequest candidateModelRequest);

    @PreAuthorize(value = "hasResourcePermission(#candidateId, @PC.PATTERN_CANDIDATE_VOTE)")
    Candidate updateCandidateRating(UUID candidateId, UUID userId, RatingModelMultiRequest ratingModelMultiRequest);

    @PreAuthorize(value = "hasResourcePermission(#candidateId, @PC.PATTERN_CANDIDATE_DELETE)")
    void deleteCandidate(UUID candidateId, UUID userId);

    /** Author */
    @PreAuthorize(value = "hasResourcePermission(#candidateId, @PC.PATTERN_CANDIDATE_EDIT)")
    Candidate saveCandidateAuthor(UUID candidateId, AuthorModelRequest authorModelRequest);

    @PreAuthorize(value = "hasResourcePermission(#candidateId, @PC.PATTERN_CANDIDATE_EDIT)")
    Candidate deleteCandidateAuthor(UUID candidateId, UUID userId);

    /** Comment */
    @PreAuthorize(value = "hasResourcePermission(#candidateId, @PC.PATTERN_CANDIDATE_COMMENT)")
    Candidate createComment(UUID candidateId, UUID userId, CommentModel commentModel);

    @PostAuthorize(value = "hasResourcePermission(returnObject.candidate.id, @PC.PATTERN_CANDIDATE_READ)")
    CandidateComment getCommentById(UUID candidateCommentId);

    @PreAuthorize(value = "hasResourcePermission(#candidateId, @PC.PATTERN_CANDIDATE_EDIT)")
    Candidate updateComment(UUID candidateId, UUID commentId, UUID userId, CommentModel commentModel);

    @PreAuthorize(value = "hasResourcePermission(#candidateId, @PC.PATTERN_CANDIDATE_VOTE)")
    Candidate updateCandidateCommentRating(UUID candidateId, UUID commentId, UUID userId, RatingModelRequest ratingModelRequest);

    @PreAuthorize(value = "hasResourcePermission(#candidateId, @PC.PATTERN_CANDIDATE_EDIT)")
    Candidate deleteComment(UUID candidateId, UUID commentId, UUID userId);

    /** Evidence */
    @PreAuthorize(value = "hasResourcePermission(#candidateId, @PC.PATTERN_CANDIDATE_EVIDENCE)")
    Candidate createEvidence(UUID candidateId, UUID userId, EvidenceModel evidenceModel);

    @PostAuthorize(value = "hasResourcePermission(returnObject.candidate.id, @PC.PATTERN_CANDIDATE_READ)")
    CandidateEvidence getEvidenceById(UUID evidenceId);

    @PreAuthorize(value = "hasResourcePermission(#candidateId, @PC.PATTERN_CANDIDATE_EDIT)")
    Candidate updateEvidence(UUID candidateId, UUID evidenceId, UUID userId, EvidenceModel evidenceModel);

    @PreAuthorize(value = "hasResourcePermission(#candidateId, @PC.PATTERN_CANDIDATE_VOTE)")
    Candidate updateCandidateEvidenceRating(UUID candidateId, UUID evidenceID, UUID userId, RatingModelRequest ratingModelRequest);

    @PreAuthorize(value = "hasResourcePermission(#candidateId, @PC.PATTERN_CANDIDATE_EDIT)")
    Candidate deleteEvidence(UUID candidateId, UUID evidenceId, UUID userId);
}