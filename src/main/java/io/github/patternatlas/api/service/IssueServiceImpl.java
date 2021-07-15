package io.github.patternatlas.api.service;

import io.github.patternatlas.api.entities.issue.IssueRating;
import io.github.patternatlas.api.entities.issue.comment.IssueComment;
import io.github.patternatlas.api.entities.issue.Issue;
import io.github.patternatlas.api.entities.issue.author.IssueAuthor;
import io.github.patternatlas.api.entities.issue.comment.IssueCommentRating;
import io.github.patternatlas.api.entities.issue.evidence.IssueEvidence;
import io.github.patternatlas.api.entities.issue.evidence.IssueEvidenceRating;
import io.github.patternatlas.api.entities.shared.AuthorConstant;
import io.github.patternatlas.api.entities.user.UserEntity;
import io.github.patternatlas.api.repositories.*;
import io.github.patternatlas.api.rest.model.issue.IssueModelRequest;
import io.github.patternatlas.api.rest.model.shared.AuthorModelRequest;
import io.github.patternatlas.api.rest.model.shared.CommentModel;
import io.github.patternatlas.api.rest.model.shared.EvidenceModel;
import io.github.patternatlas.api.rest.model.shared.RatingModelRequest;

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
    private IssueRatingRepository issueRatingRepository;
    private IssueAuthorRepository issueAuthorRepository;
    private IssueCommentRepository issueCommentRepository;
    private IssueCommentRatingRepository issueCommentRatingRepository;
    private IssueEvidenceRepository issueEvidenceRepository;
    private IssueEvidenceRatingRepository issueEvidenceRatingRepository;
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(IssueServiceImpl.class);

    public IssueServiceImpl(
            IssueRepository issueRepository,
            IssueRatingRepository issueRatingRepository,
            IssueAuthorRepository issueAuthorRepository,
            IssueCommentRepository issueCommentRepository,
            IssueCommentRatingRepository issueCommentRatingRepository,
            IssueEvidenceRepository issueEvidenceRepository,
            IssueEvidenceRatingRepository issueEvidenceRatingRepository,
            UserService userService
    ) {
        this.issueRepository = issueRepository;
        this.issueRatingRepository = issueRatingRepository;
        this.issueAuthorRepository = issueAuthorRepository;
        this.issueCommentRepository = issueCommentRepository;
        this.issueCommentRatingRepository = issueCommentRatingRepository;
        this.issueEvidenceRepository = issueEvidenceRepository;
        this.issueEvidenceRatingRepository = issueEvidenceRatingRepository;
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
    public Issue updateIssueRating(UUID issueId, UUID userId, RatingModelRequest ratingModelRequest) {
        Issue issue = this.getIssueById(issueId);
        UserEntity user = this.userService.getUserById(userId);
        IssueRating issueRating = new IssueRating(issue, user, ratingModelRequest.getRating());
        if (this.issueRatingRepository.existsByIdAndRating(issueRating.getId(), issueRating.getRating())) {
            issueRating.setRating(0);
        }
        issueRating = this.issueRatingRepository.save(issueRating);
        return issueRating.getIssue();
    }

    @Override
    @Transactional
    public void deleteIssue(UUID IssueId) {
        this.getIssueById(IssueId);
        this.issueRepository.deleteById(IssueId);
    }

    public boolean authorPermissions(UUID issueId, UUID userId) {
        if (issueId == null)
            return false;
        Issue issue = this.getIssueById(issueId);
        for (IssueAuthor author : issue.getAuthors()) {
            if (author.getUser().getId() == userId) {
                if (author.getRole().equals(AuthorConstant.OWNER)  || author.getRole().equals(AuthorConstant.MAINTAINER) ) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Author
     */
    @Override
    @Transactional
    public Issue saveIssueAuthor(UUID issueId, AuthorModelRequest authorModelRequest) {
        Issue issue = this.getIssueById(issueId);
        UserEntity user = userService.getUserById(authorModelRequest.getUserId());
        IssueAuthor issueAuthor = new IssueAuthor(issue, user, authorModelRequest.getAuthorRole());
        issueAuthor = this.issueAuthorRepository.save(issueAuthor);
        return issueAuthor.getIssue();
    }

    @Override
    @Transactional
    public Issue deleteIssueAuthor(UUID issueId, UUID userId) {
        Issue issue = this.getIssueById(issueId);
        UserEntity user = this.userService.getUserById(userId);
        IssueAuthor issueAuthor = new IssueAuthor(issue, user);
        if (this.issueAuthorRepository.existsById(issueAuthor.getId())) {
            this.issueAuthorRepository.deleteById(issueAuthor.getId());
        }
        return this.getIssueById(issueId);
    }

    /**
     * Comment
     */
    @Override
    @Transactional
    public Issue createComment(UUID issueId, UUID userId, CommentModel commentModel) {
        Issue issue = this.getIssueById(issueId);
        UserEntity user = this.userService.getUserById(userId);

        IssueComment comment = this.issueCommentRepository.save(new IssueComment(commentModel.getText(), issue, user));
        return comment.getIssue();
    }

    @Override
    @Transactional(readOnly = true)
    public IssueComment getCommentById(UUID commentId) {
        return this.issueCommentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Issue comment with ID %s not found!", commentId)));
    }

    @Override
    @Transactional
    public Issue updateComment(UUID issueId, UUID commentId, UUID userId, CommentModel commentModel) {
        if (commentModel == null)
            throw new RuntimeException("Issue comment to update is null!");
        IssueComment comment = this.existsIssueComment(issueId, commentId, userId);
        comment.updateComment(commentModel.getText());
        comment = this.issueCommentRepository.save(comment);
        return comment.getIssue();
    }

    @Override
    @Transactional
    public Issue updateIssueCommentRating(UUID issueId, UUID commentId, UUID userId, RatingModelRequest ratingModelRequest) {
        IssueComment issueComment = this.getCommentById(commentId);
        UserEntity user = this.userService.getUserById(userId);
        IssueCommentRating issueCommentRating = new IssueCommentRating(issueComment, user, ratingModelRequest.getRating());
        if (this.issueCommentRatingRepository.existsByIdAndRating(issueCommentRating.getId(), issueCommentRating.getRating())) {
            issueCommentRating.setRating(0);
        }
        issueCommentRating = this.issueCommentRatingRepository.save(issueCommentRating);
        return issueCommentRating.getIssueComment().getIssue();
    }

    @Override
    @Transactional
    public Issue deleteComment(UUID issueId, UUID commentId, UUID userId) {
        this.existsIssueComment(issueId, commentId, userId);
        this.issueCommentRepository.deleteById(commentId);
        return this.getIssueById(issueId);
    }

    // Check if Issue comment exists
    private IssueComment existsIssueComment(UUID issueId, UUID commentId, UUID userId) {
        IssueComment issueComment = this.getCommentById(commentId);
        // CORRECT Issue
        if (!issueComment.getIssue().equals(this.getIssueById(issueId)))
            throw new EntityNotFoundException(String.format("Issue comment with id %s does not belong to issue with id %s", commentId, issueId));
        // CORRECT user
        if (!issueComment.getUser().equals(this.userService.getUserById(userId)))
            throw new EntityNotFoundException(String.format("Issue comment with id %s does not belong to user with id %s", commentId, userId));
        return issueComment;
    }

    /**
     * Evidence
     */
    @Override
    @Transactional
    public Issue createEvidence(UUID issueId, UUID userId, EvidenceModel evidenceModel) {
        Issue issue = this.getIssueById(issueId);
        UserEntity user = this.userService.getUserById(userId);

        IssueEvidence issueEvidence = new IssueEvidence(evidenceModel.getTitle(), evidenceModel.getContext(), evidenceModel.getType(), evidenceModel.getSupporting(), evidenceModel.getSource(), issue, user);
        issueEvidence = this.issueEvidenceRepository.save(issueEvidence);
        return issueEvidence.getIssue();
    }

    @Override
    @Transactional(readOnly = true)
    public IssueEvidence getEvidenceById(UUID issueEvidenceId) {
        return this.issueEvidenceRepository.findById(issueEvidenceId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Issue comment with ID %s not found!", issueEvidenceId)));
    }

    @Override
    @Transactional
    public Issue updateEvidence(UUID issueId, UUID evidenceId, UUID userId, EvidenceModel evidenceModel) {
        if (evidenceModel == null)
            throw new RuntimeException("Issue comment to update is null!");
        IssueEvidence issueEvidence = this.existsIssueEvidence(issueId, evidenceId, userId);
        // UPDATE issue evidence
        issueEvidence.updateEvidence(evidenceModel.getTitle(), evidenceModel.getContext(), evidenceModel.getType(), evidenceModel.getSupporting(), evidenceModel.getSource());
        issueEvidence = this.issueEvidenceRepository.save(issueEvidence);
        return issueEvidence.getIssue();
    }

    @Override
    @Transactional
    public Issue updateIssueEvidenceRating(UUID issueId, UUID evidenceID, UUID userId, RatingModelRequest ratingModelRequest) {
        Issue issue = this.getIssueById(issueId);
        IssueEvidence issueEvidence = this.getEvidenceById(evidenceID);
        UserEntity user = this.userService.getUserById(userId);
        IssueEvidenceRating issueEvidenceRating = new IssueEvidenceRating(issueEvidence, user, ratingModelRequest.getRating());
        if (this.issueEvidenceRatingRepository.existsByIdAndRating(issueEvidenceRating.getId(), issueEvidenceRating.getRating())) {
            issueEvidenceRating.setRating(0);
        }
        issueEvidenceRating = this.issueEvidenceRatingRepository.save(issueEvidenceRating);
        return issueEvidenceRating.getIssueEvidence().getIssue();
    }

    @Override
    @Transactional
    public Issue deleteEvidence(UUID issueId, UUID evidenceId, UUID userId) {
        this.existsIssueEvidence(issueId, evidenceId, userId);
        this.issueEvidenceRepository.deleteById(evidenceId);
        return this.getIssueById(issueId);
    }

    private IssueEvidence existsIssueEvidence(UUID issueId, UUID commentId, UUID userId) {
        IssueEvidence issueEvidence = this.getEvidenceById(commentId);
        // CORRECT Issue
        if (!issueEvidence.getIssue().equals(this.getIssueById(issueId)))
            throw new EntityNotFoundException(String.format("Issue comment with id %s does not belong to issue with id %s", commentId, issueId));
        // CORRECT user
        if (!issueEvidence.getUser().equals(this.userService.getUserById(userId)))
            throw new EntityNotFoundException(String.format("Issue comment with id %s does not belong to user with id %s", commentId, userId));
        return issueEvidence;
    }
}