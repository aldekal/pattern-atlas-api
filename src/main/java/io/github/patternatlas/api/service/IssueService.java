package io.github.patternatlas.api.service;

import java.util.List;
import java.util.UUID;

import io.github.patternatlas.api.entities.issue.Issue;
import io.github.patternatlas.api.entities.issue.IssueComment;

public interface IssueService {

    /**
     * CRUD
     */
    Issue createIssue(Issue issue);

    Issue getIssueById(UUID issueId);

    Issue getIssueByURI(String uri);

    List<Issue> getAllIssues();

    Issue updateIssue(Issue issue);

    void deleteIssue(UUID issueId);

    /**
     * Voting
     */
    Issue userRating(UUID issueId, UUID userId, String rating);

    /**
     * Comment
     */
    Issue createComment(UUID issueId, UUID userId, IssueComment issueComment);

    IssueComment getCommentById(UUID issueCommentId);

    IssueComment updateComment(IssueComment issueComment);

    Issue commentUserRating(UUID issueCommentId, UUID userId, String rating);
}
