package com.patternpedia.api.service;

import com.patternpedia.api.entities.candidate.Candidate;
import com.patternpedia.api.entities.candidate.CandidateRating;
import com.patternpedia.api.entities.candidate.comment.CandidateComment;
import com.patternpedia.api.entities.candidate.comment.CandidateCommentRating;
import com.patternpedia.api.entities.candidate.evidence.CandidateEvidence;
import com.patternpedia.api.entities.candidate.evidence.CandidateEvidenceRating;
import com.patternpedia.api.entities.issue.Issue;
import com.patternpedia.api.entities.issue.IssueRating;
import com.patternpedia.api.entities.issue.comment.IssueComment;
import com.patternpedia.api.entities.issue.comment.IssueCommentRating;
import com.patternpedia.api.entities.issue.evidence.IssueEvidence;
import com.patternpedia.api.entities.issue.evidence.IssueEvidenceRating;
import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.repositories.*;
import com.patternpedia.api.rest.model.shared.RatingModel;
import com.patternpedia.api.rest.model.shared.RatingModelMultiRequest;
import com.patternpedia.api.rest.model.shared.RatingModelRequest;
import com.patternpedia.api.rest.model.shared.RatingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.UUID;

@Service
@Transactional
public class RatingServiceImpl implements RatingService {

    Logger logger = LoggerFactory.getLogger(RatingServiceImpl.class);

    private IssueRatingRepository issueRatingRepository;
    private IssueCommentRatingRepository issueCommentRatingRepository;
    private IssueEvidenceRatingRepository issueEvidenceRatingRepository;
    private IssueService issueService;
    private CandidateRatingRepository candidateRatingRepository;
    private CandidateCommentRatingRepository candidateCommentRatingRepository;
    private CandidateEvidenceRatingRepository candidateEvidenceRatingRepository;
    private CandidateService candidateService;
    private UserService userService;

    public RatingServiceImpl(
            IssueRatingRepository issueRatingRepository,
            IssueCommentRatingRepository issueCommentRatingRepository,
            IssueEvidenceRatingRepository issueEvidenceRatingRepository,
            IssueService issueService,
            CandidateRatingRepository candidateRatingRepository,
            CandidateCommentRatingRepository candidateCommentRatingRepository,
            CandidateEvidenceRatingRepository candidateEvidenceRatingRepository,
            CandidateService candidateService,
            UserService userService
    ) {
        this.issueRatingRepository = issueRatingRepository;
        this.issueCommentRatingRepository = issueCommentRatingRepository;
        this.issueEvidenceRatingRepository = issueEvidenceRatingRepository;
        this.issueService = issueService;
        this.candidateRatingRepository = candidateRatingRepository;
        this.candidateCommentRatingRepository = candidateCommentRatingRepository;
        this.candidateEvidenceRatingRepository = candidateEvidenceRatingRepository;
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
    public IssueEvidenceRating updateIssueEvidenceRating(UUID issueId, UUID evidenceID, UUID userId, RatingModelRequest ratingModelRequest) {
        Issue issue = this.issueService.getIssueById(issueId);
        IssueEvidence issueEvidence = this.issueService.getEvidenceById(evidenceID);
        UserEntity user = this.userService.getUserById(userId);
        IssueEvidenceRating issueEvidenceRating = new IssueEvidenceRating(issueEvidence, user, ratingModelRequest.getRating());
        if (this.issueEvidenceRatingRepository.existsByIdAndRating(issueEvidenceRating.getId(), issueEvidenceRating.getRating())) {
            this.issueEvidenceRatingRepository.delete(issueEvidenceRating);
            issueEvidenceRating.setRating(0);
            return issueEvidenceRating;
        }
        return this.issueEvidenceRatingRepository.save(issueEvidenceRating);
    }

    @Override
    @Transactional
    public RatingModel updateCandidateRating(UUID candidateId, UUID userId, RatingModelMultiRequest ratingModelMultiRequest) {
        Candidate candidate = this.candidateService.getCandidateById(candidateId);
        UserEntity user = this.userService.getUserById(userId);
        CandidateRating candidateRating = new CandidateRating(candidate, user);
        if (this.candidateRatingRepository.existsById(candidateRating.getId())) {
            candidateRating = this.candidateRatingRepository.getOne(candidateRating.getId());
        }
        if (ratingModelMultiRequest.getRatingType().equals(RatingType.READABILITY)) {
            candidateRating.setReadability(ratingModelMultiRequest.getRating());
//            if (this.candidateRatingRepository.existsByIdAndReadability(candidateRating.getId(), candidateRating.getReadability())) {
//                candidateRating.setReadability(-1);
//                return new RatingModel(candidateRating, candidateRating.getReadability());
//            }

        } else if (ratingModelMultiRequest.getRatingType().equals(RatingType.UNDERSTANDABILITY)) {
            candidateRating.setUnderstandability(ratingModelMultiRequest.getRating());
//            if (this.candidateRatingRepository.existsByIdAndUnderstandability(candidateRating.getId(), candidateRating.getUnderstandability())) {
//                candidateRating.setUnderstandability(-1);
//                return new RatingModel(candidateRating, candidateRating.getUnderstandability());
//            }

        } else if (ratingModelMultiRequest.getRatingType().equals(RatingType.APPROPIATENESS)) {
            candidateRating.setAppropriateness(ratingModelMultiRequest.getRating());
//            if (this.candidateRatingRepository.existsByIdAndAppropriateness(candidateRating.getId(), candidateRating.getAppropriateness())) {
//                candidateRating.setAppropriateness(-1);
//                return new RatingModel(candidateRating, candidateRating.getAppropriateness());
//            }

        } else {
            throw new EntityExistsException(String.format("Rating type does not exists", ratingModelMultiRequest.getRatingType()));
        }
        CandidateRating updatedCandidateRating = this.candidateRatingRepository.save(candidateRating);

        if (ratingModelMultiRequest.getRatingType().equals(RatingType.READABILITY)) {
            return new RatingModel(updatedCandidateRating, updatedCandidateRating.getReadability());
        } else if (ratingModelMultiRequest.getRatingType().equals(RatingType.UNDERSTANDABILITY)) {
            return new RatingModel(updatedCandidateRating, updatedCandidateRating.getUnderstandability());
        } else{
            return new RatingModel(updatedCandidateRating, updatedCandidateRating.getAppropriateness());
        }

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

    @Override
    @Transactional
    public CandidateEvidenceRating updateCandidateEvidenceRating(UUID candidateId, UUID evidenceID, UUID userId, RatingModelRequest ratingModelRequest) {
        Candidate candidate = this.candidateService.getCandidateById(candidateId);
        CandidateEvidence candidateEvidence = this.candidateService.getEvidenceById(evidenceID);
        UserEntity user = this.userService.getUserById(userId);
        CandidateEvidenceRating candidateEvidenceRating = new CandidateEvidenceRating(candidateEvidence, user, ratingModelRequest.getRating());
        if (this.candidateEvidenceRatingRepository.existsByIdAndRating(candidateEvidenceRating.getId(), candidateEvidenceRating.getRating())) {
            this.candidateEvidenceRatingRepository.delete(candidateEvidenceRating);
            candidateEvidenceRating.setRating(0);
            return candidateEvidenceRating;
        }
        return this.candidateEvidenceRatingRepository.save(candidateEvidenceRating);
    }
}
