package com.patternpedia.api.service;

import com.patternpedia.api.entities.candidate.Candidate;
import com.patternpedia.api.entities.candidate.CandidateComment;
import com.patternpedia.api.entities.issue.Issue;
import com.patternpedia.api.entities.issue.IssueComment;

import java.util.List;
import java.util.UUID;

public interface CandidateService {
    /** CRUD  */
    Candidate createCandidate(Candidate candidate, UUID patternLanguageId);

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
