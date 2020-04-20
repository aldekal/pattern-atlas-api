package com.patternpedia.api.service;

import com.patternpedia.api.entities.issue.CommentIssue;
import com.patternpedia.api.entities.issue.Issue;
import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.entities.rating.RatingIssue;
import com.patternpedia.api.exception.NullPatternException;
import com.patternpedia.api.exception.PatternLanguageNotFoundException;
import com.patternpedia.api.exception.PatternNotFoundException;
import com.patternpedia.api.repositories.CommentIssueRepository;
import com.patternpedia.api.repositories.IssueRepository;
import com.patternpedia.api.repositories.RatingIssueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class IssueServiceImpl implements IssueService {

    private IssueRepository issueRepository;
    private RatingIssueRepository ratingIssueRepository;
    private CommentIssueRepository commentIssueRepository;
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(IssueServiceImpl.class);

    public IssueServiceImpl(
            IssueRepository issueRepository,
            RatingIssueRepository ratingIssueRepository,
            UserService userService,
            CommentIssueRepository commentIssueRepository
    ) {
        this.issueRepository = issueRepository;
        this.ratingIssueRepository = ratingIssueRepository;
        this.commentIssueRepository = commentIssueRepository;
        this.userService = userService;
    }

    /**
     * CRUD Issue
     */
    @Override
    @Transactional
    public Issue createIssue(Issue issue) {
        if (null == issue) {
            throw new NullPatternException();
        }

//        if (null == patternEvolution.getPatternLanguage()) {
//            throw new NullPatternLanguageException();
//        }

        return this.issueRepository.save(issue);
    }

    @Override
    @Transactional
    public Issue updateIssue(Issue issue) {
        if (null == issue) {
            throw new NullPatternException();
        }
        if (!this.issueRepository.existsById(issue.getId())) {
            throw new PatternLanguageNotFoundException(String.format("Issue %s not found", issue.getId()));
        }

        logger.info(String.format("Update Issue: %s", issue.toString()));
        return this.issueRepository.save(issue);
    }

    @Override
    @Transactional
    public void deleteIssue(UUID IssueId) {
        Issue issue = this.getIssueById(IssueId);
        if (null == issue) {
            throw new NullPatternException();
        }

        // patternEvolution.setPatternViews(null);
        // this.patternEvolutionRepository.save(patternEvolution);
        this.issueRepository.deleteById(IssueId);
    }

    @Override
    @Transactional(readOnly = true)
    public Issue getIssueById(UUID IssueId) {
        return this.issueRepository.findById(IssueId)
                .orElseThrow(() -> new PatternNotFoundException(IssueId));
    }

    @Override
    @Transactional(readOnly = true)
    public Issue getIssueByURI(String uri) {
        return this.issueRepository.findByUri(uri)
                .orElseThrow(() -> new PatternNotFoundException(String.format("Pattern with URI %s not found!", uri)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Issue> getAllIssues() {
        return this.issueRepository.findAll();
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

        RatingIssue ratingIssue = new RatingIssue(issue, user);

        if (this.ratingIssueRepository.existsById(ratingIssue.getId())) {
            ratingIssue = this.ratingIssueRepository.findByIssueAndUser(issue, user);
            logger.info(String.format("Rating for user %s exists with rating %s", user.getId(), ratingIssue.getRating()));
        } else {
            logger.info(String.format("Rating for user %s does not exist", user.getId()));
        }

        if (rating.equals("up")) {
            if (ratingIssue.getRating() == 1) {
                ratingIssue.setRating(0);
                logger.info(String.format("User %s deleted up", user.getId()));
            } else {
                ratingIssue.setRating(1);
                logger.info(String.format("User %s rated up", user.getId()));
            }

        } else if (rating.equals("down")) {
            if (ratingIssue.getRating() == -1) {
                ratingIssue.setRating(0);
                logger.info(String.format("User %s deleted down", user.getId()));
            } else {
                ratingIssue.setRating(-1);
                logger.info(String.format("User %s rated down", user.getId()));
            }
        } else {
            logger.info(String.format("Wrong rating value: %s", rating));
            return null;
        }

        issue.getUserRating().add(ratingIssue);
        int issueRating = issue.getUserRating().stream().mapToInt(RatingIssue::getRating).sum();
        issue.setRating(issueRating);
        logger.info(String.format("New rating is: %d", issueRating));

        return this.updateIssue(issue);
    }

    /**
     * Comment Issue
     */
    @Override
    @Transactional
    public Issue createComment(UUID issueId, UUID userId, CommentIssue commentIssue) {
        Issue issue = this.getIssueById(issueId);
        UserEntity user = this.userService.getUserById(userId);

        CommentIssue comment = new CommentIssue(commentIssue.getText());
        issue.addComment(comment, user);

        return this.updateIssue(issue);
    }


}
