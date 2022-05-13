package io.github.patternatlas.api.service;

import java.util.List;

import io.github.patternatlas.api.entities.issue.IssueRating;
import io.github.patternatlas.api.entities.issue.author.IssueAuthor;
import io.github.patternatlas.api.entities.issue.comment.IssueComment;
import io.github.patternatlas.api.entities.issue.Issue;
import io.github.patternatlas.api.entities.issue.comment.IssueCommentRating;
import io.github.patternatlas.api.entities.issue.evidence.IssueEvidence;
import io.github.patternatlas.api.entities.issue.evidence.IssueEvidenceRating;
import io.github.patternatlas.api.rest.model.issue.IssueModelRequest;
import io.github.patternatlas.api.rest.model.shared.AuthorModelRequest;
import io.github.patternatlas.api.rest.model.shared.CommentModel;
import io.github.patternatlas.api.rest.model.shared.EvidenceModel;
import io.github.patternatlas.api.rest.model.shared.RatingModelRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.UUID;

public interface IssueService {

    Issue saveIssue(Issue issue);
    /** CRUD  */
    @PreAuthorize(value = "hasGlobalPermission(@PC.ISSUE_CREATE)")
    Issue createIssue(IssueModelRequest issueModelRequest, UUID userId);

    @PostFilter("hasResourcePermission(filterObject.id, @PC.ISSUE_READ)")
    List<Issue> getAllIssues();

    @PreAuthorize(value = "hasResourcePermission(#issueId, @PC.ISSUE_READ)")
    Issue getIssueById(UUID issueId);

    @PostAuthorize(value = "hasResourcePermission(returnObject.id, @PC.ISSUE_READ)")
    Issue getIssueByURI(String uri);

    @PreAuthorize(value = "hasResourcePermission(#issueId, @PC.ISSUE_EDIT)")
    Issue updateIssue(UUID issueId, UUID userId, IssueModelRequest issueModelRequest);

    @PreAuthorize(value = "hasResourcePermission(#issueId, @PC.ISSUE_VOTE)")
    Issue updateIssueRating(UUID issueId, UUID userId, RatingModelRequest ratingModelRequest);

    @PreAuthorize(value = "hasResourcePermission(#issueId, @PC.ISSUE_DELETE)")
    void deleteIssue(UUID issueId);

    /** Author */
    @PreAuthorize(value = "hasResourcePermission(#issueId, @PC.ISSUE_EDIT)")
    Issue saveIssueAuthor(UUID issueId, AuthorModelRequest authorModelRequest);

    @PreAuthorize(value = "hasResourcePermission(#issueId, @PC.ISSUE_EDIT)")
    Issue deleteIssueAuthor(UUID issueId, UUID userId);

    /** Comment */
    @PreAuthorize(value = "hasResourcePermission(#issueId, @PC.ISSUE_COMMENT)")
    Issue createComment(UUID issueId, UUID userId, CommentModel commentModel);

    @PostAuthorize(value = "hasResourcePermission(returnObject.issue.id, @PC.ISSUE_READ)")
    IssueComment getCommentById(UUID issueCommentId);

    @PreAuthorize(value = "hasResourcePermission(#issueId, @PC.ISSUE_EDIT)")
    Issue updateComment(UUID issueId, UUID commentId, UUID userId, CommentModel commentModel);

    @PreAuthorize(value = "hasResourcePermission(#issueId, @PC.ISSUE_VOTE)")
    Issue updateIssueCommentRating(UUID issueId, UUID commentId, UUID userId, RatingModelRequest ratingModelRequest);

    @PreAuthorize(value = "hasResourcePermission(#issueId, @PC.ISSUE_EDIT)")
    Issue deleteComment(UUID issueId, UUID commentId, UUID userId);

    /** Evidence */
    @PreAuthorize(value = "hasResourcePermission(#issueId, @PC.ISSUE_EVIDENCE)")
    Issue createEvidence(UUID issueId, UUID userId, EvidenceModel evidenceModel);

    @PostAuthorize(value = "hasResourcePermission(returnObject.issue.id, @PC.ISSUE_READ)")
    IssueEvidence getEvidenceById(UUID issueEvidenceId);

    @PreAuthorize(value = "hasResourcePermission(#issueId, @PC.ISSUE_EDIT)")
    Issue updateEvidence(UUID issueId, UUID evidenceId, UUID userId, EvidenceModel evidenceModel);

    @PreAuthorize(value = "hasResourcePermission(#issueId, @PC.ISSUE_VOTE)")
    Issue updateIssueEvidenceRating(UUID issueId, UUID evidenceID, UUID userId, RatingModelRequest ratingModelRequest);

    @PreAuthorize(value = "hasResourcePermission(#issueId, @PC.ISSUE_EDIT)")
    Issue deleteEvidence(UUID issueId, UUID evidenceId, UUID userId);


}