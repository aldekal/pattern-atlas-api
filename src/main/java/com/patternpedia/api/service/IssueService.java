package com.patternpedia.api.service;

import java.util.List;

import com.patternpedia.api.entities.issue.CommentIssue;
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
    Issue createComment(UUID issueId, UUID userId, CommentIssue commentIssue);
}
