package com.patternpedia.api.service;

import java.util.List;

import com.patternpedia.api.entities.issue.IssueComment;
import com.patternpedia.api.entities.issue.Issue;

import java.util.UUID;

public interface IssueService {

    /** CRUD  */
    Issue createIssue(Issue issue);

    Issue updateIssue(Issue issue);

    void deleteIssue(UUID issueId);

    Issue getIssueById(UUID issueId);

    Issue getIssueByURI(String uri);

    List<Issue> getAllIssues();

    /** Voting */
    Issue userRating(UUID issueId, UUID userId, String rating);

    /** Comment */
    Issue createComment(UUID issueId, UUID userId, IssueComment issueComment);

    IssueComment getCommentById(UUID issueCommentId);

    IssueComment updateComment(IssueComment issueComment);

    Issue commentUserRating(UUID issueCommentId, UUID userId, String rating);
}
