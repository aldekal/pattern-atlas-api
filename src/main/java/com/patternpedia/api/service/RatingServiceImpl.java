package com.patternpedia.api.service;

import com.patternpedia.api.entities.candidate.Candidate;
import com.patternpedia.api.entities.candidate.CandidateRating;
import com.patternpedia.api.entities.candidate.comment.CandidateComment;
import com.patternpedia.api.entities.candidate.comment.CandidateCommentRating;
import com.patternpedia.api.entities.issue.Issue;
import com.patternpedia.api.entities.issue.IssueRating;
import com.patternpedia.api.entities.issue.comment.IssueComment;
import com.patternpedia.api.entities.issue.comment.IssueCommentRating;
import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.repositories.*;
import com.patternpedia.api.rest.model.shared.RatingModelRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class RatingServiceImpl implements RatingService {

    Logger logger = LoggerFactory.getLogger(RatingServiceImpl.class);

    private IssueRatingRepository issueRatingRepository;
    private IssueCommentRatingRepository issueCommentRatingRepository;
    private IssueService issueService;
    private CandidateRatingRepository candidateRatingRepository;
    private CandidateCommentRatingRepository candidateCommentRatingRepository;
    private CandidateService candidateService;
    private UserService userService;

    public RatingServiceImpl(
            IssueRatingRepository issueRatingRepository,
            IssueCommentRatingRepository issueCommentRatingRepository,
            IssueService issueService,
            CandidateRatingRepository candidateRatingRepository,
            CandidateCommentRatingRepository candidateCommentRatingRepository,
            CandidateService candidateService,
            UserService userService
    ) {
        this.issueRatingRepository = issueRatingRepository;
        this.issueCommentRatingRepository = issueCommentRatingRepository;
        this.issueService = issueService;
        this.candidateRatingRepository = candidateRatingRepository;
        this.candidateCommentRatingRepository = candidateCommentRatingRepository;
        this.candidateService = candidateService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public IssueRating updateIssueRating(UUID issueId, UUID userId, RatingModelRequest ratingModelRequest) {
        Issue issue = this.issueService.getIssueById(issueId);
        UserEntity user = this.userService.getUserById(userId);
        IssueRating issueRating = new IssueRating(issue, user, ratingModelRequest.getRating());

        if (this.issueRatingRepository.existsByIdAndRating(issueRating.getId(), issueRating.getRating())) {
            this.issueRatingRepository.delete(issueRating);
            issueRating.setRating(0);
            return issueRating;
        }
        return this.issueRatingRepository.save(issueRating);
    }

    @Override
    @Transactional
    public IssueCommentRating updateIssueCommentRating(UUID issueId, UUID commentId, UUID userId, RatingModelRequest ratingModelRequest) {
        Issue issue = this.issueService.getIssueById(issueId);
        IssueComment issueComment = this.issueService.getCommentById(commentId);
        UserEntity user = this.userService.getUserById(userId);
        IssueCommentRating issueCommentRating = new IssueCommentRating(issueComment, user, ratingModelRequest.getRating());
        if (this.issueCommentRatingRepository.existsByIdAndRating(issueCommentRating.getId(), issueCommentRating.getRating())) {
            this.issueCommentRatingRepository.delete(issueCommentRating);
            issueCommentRating.setRating(0);
            return issueCommentRating;
        }
        return this.issueCommentRatingRepository.save(issueCommentRating);
    }

    @Override
    @Transactional
    public CandidateRating updateCandidateRating(UUID candidateId, UUID userId, RatingModelRequest ratingModelRequest) {
        Candidate candidate = this.candidateService.getCandidateById(candidateId);
        UserEntity user = this.userService.getUserById(userId);
        CandidateRating candidateRating = new CandidateRating(candidate, user, ratingModelRequest.getRating());
        if (this.candidateRatingRepository.existsByIdAndRating(candidateRating.getId(), candidateRating.getRating())) {
            candidateRating.setRating(0);
        }
        return this.candidateRatingRepository.save(candidateRating);
    }

    @Override
    @Transactional
    public CandidateCommentRating updateCandidateCommentRating(UUID candidateId, UUID commentId, UUID userId, RatingModelRequest ratingModelRequest) {
        Candidate candidate = this.candidateService.getCandidateById(candidateId);
        CandidateComment candidateComment = this.candidateService.getCommentById(commentId);
        UserEntity user = this.userService.getUserById(userId);
        CandidateCommentRating candidateCommentRating = new CandidateCommentRating(candidateComment, user, ratingModelRequest.getRating());
        if (this.candidateCommentRatingRepository.existsByIdAndRating(candidateCommentRating.getId(), candidateCommentRating.getRating())) {
            this.candidateCommentRatingRepository.delete(candidateCommentRating);
            candidateCommentRating.setRating(0);
            return candidateCommentRating;
        }
        return this.candidateCommentRatingRepository.save(candidateCommentRating);
    }
}
