package io.github.ust.quantil.patternatlas.api.service;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.ust.quantil.patternatlas.api.entities.issue.Issue;
import io.github.ust.quantil.patternatlas.api.entities.issue.IssueComment;
import io.github.ust.quantil.patternatlas.api.entities.issue.rating.IssueCommentRating;
import io.github.ust.quantil.patternatlas.api.entities.issue.rating.IssueRating;
import io.github.ust.quantil.patternatlas.api.entities.user.UserEntity;
import io.github.ust.quantil.patternatlas.api.exception.CommentNotFoundException;
import io.github.ust.quantil.patternatlas.api.exception.IssueNotFoundException;
import io.github.ust.quantil.patternatlas.api.exception.NullCommentException;
import io.github.ust.quantil.patternatlas.api.exception.NullIssueException;
import io.github.ust.quantil.patternatlas.api.repositories.IssueCommentRatingRepository;
import io.github.ust.quantil.patternatlas.api.repositories.IssueCommentRepository;
import io.github.ust.quantil.patternatlas.api.repositories.IssueRatingRepository;
import io.github.ust.quantil.patternatlas.api.repositories.IssueRepository;
import io.github.ust.quantil.patternatlas.api.util.RatingHelper;

@Service
@Transactional
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final IssueRatingRepository issueRatingRepository;
    private final IssueCommentRepository issueCommentRepository;
    private final IssueCommentRatingRepository issueCommentRatingRepository;
    private final UserService userService;
    private final RatingHelper ratingHelper;

    Logger logger = LoggerFactory.getLogger(IssueServiceImpl.class);

    public IssueServiceImpl(
            IssueRepository issueRepository,
            IssueRatingRepository issueRatingRepository,
            IssueCommentRepository issueCommentRepository,
            IssueCommentRatingRepository issueCommentRatingRepository,
            UserService userService
    ) {
        this.issueRepository = issueRepository;
        this.issueRatingRepository = issueRatingRepository;
        this.issueCommentRepository = issueCommentRepository;
        this.issueCommentRatingRepository = issueCommentRatingRepository;
        this.userService = userService;
        this.ratingHelper = new RatingHelper();
    }

    /**
     * CRUD Issue
     */
    @Override
    @Transactional
    public Issue createIssue(Issue issue) {
        if (null == issue) {
            throw new NullIssueException();
        }

        Issue newIssue = this.issueRepository.save(issue);
        logger.info(String.format("Create Issue %s: ", newIssue.toString()));
        return newIssue;
    }

    @Override
    @Transactional(readOnly = true)
    public Issue getIssueById(UUID IssueId) {
        return this.issueRepository.findById(IssueId)
                .orElseThrow(() -> new IssueNotFoundException(IssueId));
    }

    @Override
    @Transactional(readOnly = true)
    public Issue getIssueByURI(String uri) {
        return this.issueRepository.findByUri(uri)
                .orElseThrow(() -> new IssueNotFoundException(String.format("Issue with URI %s not found!", uri)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Issue> getAllIssues() {
        return this.issueRepository.findAll();
    }

    @Override
    @Transactional
    public Issue updateIssue(Issue issue) {
        if (null == issue) {
            throw new NullIssueException();
        }
        if (!this.issueRepository.existsById(issue.getId())) {
            throw new IssueNotFoundException(String.format("Issue %s not found", issue.getId()));
        }

        logger.info(String.format("Update Issue: %s", issue.toString()));
        return this.issueRepository.save(issue);
    }

    @Override
    @Transactional
    public void deleteIssue(UUID IssueId) {
        Issue issue = this.getIssueById(IssueId);
        if (null == issue) {
            throw new NullIssueException();
        }

        this.issueRepository.deleteById(IssueId);
    }

    /**
     * Voting Issue
     *
     * @param rating up, down
     * @apiNote If user already up- or downvoted and does so again it neutralises vote.
     */
    @Override
    @Transactional
    public Issue userRating(UUID IssueId, UUID userId, String rating) {
        Issue issue = this.getIssueById(IssueId);
        UserEntity user = this.userService.getUserById(userId);

        logger.info(String.format("User %s updates rating for Issue %s", user.getId(), issue.getId()));

        IssueRating issueRating = new IssueRating(issue, user);

        if (this.issueRatingRepository.existsById(issueRating.getId())) {
            issueRating = this.issueRatingRepository.findByIssueAndUser(issue, user);
            logger.info(String.format("Rating for user %s exists with rating %s", user.getId(), issueRating.getRating()));
        } else {
            logger.info(String.format("Rating for user %s does not exist", user.getId()));
        }

        int newRating = this.ratingHelper.updateRating(rating, issueRating.getRating(), user);
        if (newRating == -2) {
            return null;
        } else {
            issueRating.setRating(newRating);
        }

        issue.getUserRating().add(issueRating);
        int updateRating = issue.getUserRating().stream().mapToInt(IssueRating::getRating).sum();
        issue.setRating(updateRating);

        return this.updateIssue(issue);
    }

    /**
     * Comment Issue
     */
    @Override
    @Transactional
    public Issue createComment(UUID issueId, UUID userId, IssueComment issueComment) {
        Issue issue = this.getIssueById(issueId);
        UserEntity user = this.userService.getUserById(userId);

        IssueComment comment = new IssueComment(issueComment.getText());
        issue.addComment(comment, user);

        return this.updateIssue(issue);
    }

    @Override
    @Transactional
    public IssueComment updateComment(IssueComment issueComment) {
        if (null == issueComment) {
            throw new NullCommentException();
        }
        if (!this.issueCommentRepository.existsById(issueComment.getId())) {
            throw new CommentNotFoundException(String.format("Comment %s for issue %s not found", issueComment.getId(), issueComment.getIssue().getId()));
        }

        logger.info(String.format("Update issue comment: %s", issueComment.toString()));
        return this.issueCommentRepository.save(issueComment);
    }

    @Override
    @Transactional(readOnly = true)
    public IssueComment getCommentById(UUID commentId) {
        return this.issueCommentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
    }

    /**
     * Voting IssueComment
     *
     * @param rating up, down
     * @apiNote If user already up- or downvoted and does so again it neutralises vote.
     */
    @Override
    @Transactional
    public Issue commentUserRating(UUID issueCommentId, UUID userId, String rating) {
        IssueComment issueComment = this.getCommentById(issueCommentId);
        UserEntity user = this.userService.getUserById(userId);

        logger.info(String.format("User %s updates rating for Comment %s", user.getId(), issueComment.getId()));

        IssueCommentRating issueCommentRating = new IssueCommentRating(issueComment, user);

        if (this.issueCommentRatingRepository.existsById(issueCommentRating.getId())) {
            issueCommentRating = this.issueCommentRatingRepository.findByIssueCommentAndUser(issueComment, user);
            logger.info(String.format("Rating for user %s exists with rating %s", user.getId(), issueCommentRating.getRating()));
        } else {
            logger.info(String.format("Rating for user %s does not exist", user.getId()));
        }

        int newRating = this.ratingHelper.updateRating(rating, issueCommentRating.getRating(), user);
        if (newRating == -2) {
            return null;
        } else {
            issueCommentRating.setRating(newRating);
        }

        issueComment.getUserRating().add(issueCommentRating);
        int updateRating = issueComment.getUserRating().stream().mapToInt(IssueCommentRating::getRating).sum();
        issueComment.setRating((updateRating));
        logger.info(String.format("New rating for comment is: %d", updateRating));
        this.updateComment(issueComment);

        return this.getIssueById(issueComment.getIssue().getId());
    }
}
