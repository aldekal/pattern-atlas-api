package com.patternpedia.api.service;

import java.util.List;

import com.patternpedia.api.entities.issue.comment.IssueComment;
import com.patternpedia.api.entities.issue.Issue;
import com.patternpedia.api.entities.issue.evidence.IssueEvidence;
import com.patternpedia.api.rest.model.issue.IssueModelRequest;
import com.patternpedia.api.rest.model.shared.CommentModel;
import com.patternpedia.api.rest.model.shared.EvidenceModel;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IssueService {

    Issue saveIssue(Issue issue);
    /** CRUD  */
    Issue createIssue(IssueModelRequest issueModelRequest, UUID userId);

    List<Issue> getAllIssues();

    Issue getIssueById(UUID issueId);

    Issue getIssueByURI(String uri);

    Issue updateIssue(UUID issueId, UUID userId, IssueModelRequest issueModelRequest);

    void deleteIssue(UUID issueId);

    boolean authorPermissions(UUID issueId, UUID userId);

    /** Comment */
    IssueComment createComment(UUID issueId, UUID userId, CommentModel commentModel);

    IssueComment getCommentById(UUID issueCommentId);

    IssueComment updateComment(UUID issueId, UUID commentId, UUID userId, CommentModel commentModel);

    ResponseEntity<?> deleteComment(UUID issueId, UUID commentId, UUID userId);

    /** Evidence */
    IssueEvidence createEvidence(UUID issueId, UUID userId, EvidenceModel evidenceModel);

    IssueEvidence getEvidenceById(UUID issueEvidenceId);

    IssueEvidence updateEvidence(UUID issueId, UUID evidenceId, UUID userId, EvidenceModel evidenceModel);

    ResponseEntity<?> deleteEvidence(UUID issueId, UUID evidenceId, UUID userId);


}
