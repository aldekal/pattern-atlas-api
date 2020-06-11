package com.patternpedia.api.service;

import com.patternpedia.api.entities.issue.comment.IssueComment;
import com.patternpedia.api.entities.issue.Issue;
import com.patternpedia.api.entities.issue.author.IssueAuthor;
import com.patternpedia.api.entities.issue.comment.IssueCommentRating;
import com.patternpedia.api.entities.shared.AuthorConstant;
import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.repositories.*;
import com.patternpedia.api.rest.model.issue.IssueModelRequest;
import com.patternpedia.api.rest.model.shared.AuthorModel;
import com.patternpedia.api.rest.model.shared.CommentModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
@Transactional
public class IssueServiceImpl implements IssueService {

    private IssueRepository issueRepository;
    private IssueCommentRepository issueCommentRepository;
    private IssueCommentRatingRepository issueCommentRatingRepository;
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(IssueServiceImpl.class);

    public IssueServiceImpl(
            IssueRepository issueRepository,
            IssueCommentRepository issueCommentRepository,
            IssueCommentRatingRepository issueCommentRatingRepository,
            UserService userService
    ) {
        this.issueRepository = issueRepository;
        this.issueCommentRepository = issueCommentRepository;
        this.issueCommentRatingRepository = issueCommentRatingRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Issue saveIssue(Issue issue) {
        return this.issueRepository.save(issue);
    }

    /**
     * CRUD Issue
     */
    @Override
    @Transactional
    public Issue createIssue(IssueModelRequest issueModelRequest, UUID userId) {
        Issue issue = new Issue(issueModelRequest);
        if (null == issue)
            throw new RuntimeException("Issue to create is null");
        if (this.issueRepository.existsByName(issueModelRequest.getName()))
            throw new EntityExistsException(String.format("Issue name %s already exist!", issueModelRequest.getName()));
        if (this.issueRepository.existsByUri(issueModelRequest.getUri()))
            throw new EntityExistsException(String.format("Issue uri %s already exist!", issueModelRequest.getUri()));

        Issue newIssue = this.issueRepository.save(issue);
        newIssue.getAuthors().add(new IssueAuthor(newIssue, this.userService.getUserById(userId), AuthorConstant.OWNER));
        return this.issueRepository.save(newIssue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Issue> getAllIssues() {
        return this.issueRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Issue getIssueById(UUID issueId) {
        return this.issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Issue with ID %s not found!", issueId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Issue getIssueByURI(String uri) {
        return this.issueRepository.findByUri(uri)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Issue with URI %s not found!", uri)));
    }

    @Override
    @Transactional
    public Issue updateIssue(UUID issueId, UUID userId, IssueModelRequest issueModelRequest) {
        if (issueModelRequest == null) {
            throw new RuntimeException("Issue to update is null!");
        }
        Issue issue = this.getIssueById(issueId);

        // UPDATE issue fields
        if (this.issueRepository.existsByUri(issueModelRequest.getUri())) {
            Issue issueUri = this.getIssueByURI(issueModelRequest.getUri());
            // NOT uri & name change
            if (!issueUri.getId().equals(issue.getId())) {
                throw new EntityExistsException(String.format("Issue uri %s already exist!", issueModelRequest.getUri()));
            }
        }
        issue.updateIssue(issueModelRequest);
        return this.issueRepository.save(issue);
    }

    @Override
    @Transactional
    public void deleteIssue(UUID IssueId) {
        this.getIssueById(IssueId);
        this.issueRepository.deleteById(IssueId);
    }

    /**
     * Comment
     */
    @Override
    @Transactional
    public IssueComment createComment(UUID issueId, UUID userId, CommentModel commentModel) {
        Issue issue = this.getIssueById(issueId);
        UserEntity user = this.userService.getUserById(userId);

        IssueComment comment = new IssueComment(commentModel.getText(), issue, user);
        return this.issueCommentRepository.save(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public IssueComment getCommentById(UUID commentId) {
        return this.issueCommentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Issue comment with ID %s not found!", commentId)));
    }

    @Override
    @Transactional
    public IssueComment updateComment(UUID issueId, UUID commentId, UUID userId, CommentModel commentModel) {
        if (commentModel == null)
            throw new RuntimeException("Issue comment to update is null!");
        IssueComment issueComment = this.authIssueComment(issueId, commentId, userId);
        // UPDATE issue comment
        issueComment.updateComment(commentModel.getText());
        return this.issueCommentRepository.save(issueComment);
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteComment(UUID issueId, UUID commentId, UUID userId) {
        this.authIssueComment(issueId, commentId, userId);
        this.issueCommentRepository.deleteById(commentId);
        return ResponseEntity.noContent().build();
    }

    private IssueComment authIssueComment(UUID issueId, UUID commentId, UUID userId) {
        IssueComment issueComment = this.getCommentById(commentId);
        // CORRECT Issue
        if (!issueComment.getIssue().equals(this.getIssueById(issueId)))
            throw new EntityNotFoundException(String.format("Issue comment with id %s does not belong to issue with id %s", commentId, issueId));
        // CORRECT user
        if (!issueComment.getUser().equals(this.userService.getUserById(userId)))
            throw new EntityNotFoundException(String.format("Issue comment with id %s does not belong to user with id %s", commentId, userId));
        return issueComment;
    }
}
