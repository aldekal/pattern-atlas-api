package com.patternpedia.api.service;

import com.patternpedia.api.entities.candidate.Candidate;
import com.patternpedia.api.entities.candidate.comment.CandidateComment;
import com.patternpedia.api.entities.candidate.author.CandidateAuthor;
import com.patternpedia.api.entities.candidate.comment.CandidateCommentRating;
import com.patternpedia.api.entities.issue.comment.IssueComment;
import com.patternpedia.api.entities.shared.AuthorConstant;
import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.repositories.*;
import com.patternpedia.api.rest.model.candidate.CandidateModelRequest;
import com.patternpedia.api.rest.model.shared.AuthorModel;
import com.patternpedia.api.rest.model.shared.CommentModel;
import com.patternpedia.api.util.RatingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CandidateServiceImpl implements CandidateService {

    private CandidateRepository candidateRepository;
    private CandidateRatingRepository candidateRatingRepository;
    private CandidateCommentRepository candidateCommentRepository;
    private PatternLanguageService patternLanguageService;
    private UserService userService;
    private RatingHelper ratingHelper;

    Logger logger = LoggerFactory.getLogger(IssueServiceImpl.class);

    public CandidateServiceImpl(
            CandidateRepository candidateRepository,
            CandidateRatingRepository candidateRatingRepository,
            CandidateCommentRepository candidateCommentRepository,
            PatternLanguageService patternLanguageService,
            UserService userService
    ) {
        this.candidateRepository = candidateRepository;
        this.candidateRatingRepository = candidateRatingRepository;
        this.candidateCommentRepository = candidateCommentRepository;
        this.patternLanguageService = patternLanguageService;
        this.userService = userService;
        this.ratingHelper = new RatingHelper();
    }

    @Override
    @Transactional
    public Candidate saveCandidate(Candidate candidate) {
        return this.candidateRepository.save(candidate);
    }

    /**
     * CRUD Candidate
     */
    @Override
    @Transactional
    public Candidate createCandidate(CandidateModelRequest candidateModelRequest, UUID userId) {
        Candidate candidate = new Candidate(candidateModelRequest);
        if (null == candidate)
            throw new RuntimeException("Candidate to create is null");
        if (this.candidateRepository.existsByName(candidate.getName()))
            throw new EntityExistsException(String.format("Candidate name %s already exist!", candidateModelRequest.getName()));
        if (this.candidateRepository.existsByUri(candidateModelRequest.getUri()))
            throw new EntityExistsException(String.format("Candidate uri %s already exist!", candidateModelRequest.getUri()));


//            throw new ResourceNotFoundException(String.format("Pattern Language %s does not exist", candidateModelRequest.getPatternLanguageId()));

        // ADD authors
        Candidate newCandidate = this.candidateRepository.save(candidate);
        if (candidateModelRequest.getAuthors() != null) {
            for (AuthorModel authorModel : candidateModelRequest.getAuthors()) {
                newCandidate.getAuthors().add(new CandidateAuthor(newCandidate, this.userService.getUserById(authorModel.getUserId()), authorModel.getAuthorRole()));
            }
        } else {
            newCandidate.getAuthors().add(new CandidateAuthor(newCandidate, this.userService.getUserById(userId), AuthorConstant.OWNER));
        }


        // ADD pattern language
        if (candidateModelRequest.getPatternLanguageId() != null && this.patternLanguageService.getPatternLanguageById(candidateModelRequest.getPatternLanguageId()) != null)
            newCandidate.setPatternLanguage(this.patternLanguageService.getPatternLanguageById(candidateModelRequest.getPatternLanguageId()));

        return this.candidateRepository.save(newCandidate);
    }

    @Override
    @Transactional
    public List<Candidate> getAllCandidates() {
        return this.candidateRepository.findAll();
    }

    @Override
    @Transactional
    public Candidate getCandidateById(UUID candidateId) {
        return this.candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Candidate with ID %s not found!", candidateId)));
    }

    @Override
    @Transactional
    public Candidate getCandidateByURI(String uri) {
        return this.candidateRepository.findByUri(uri)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Candidate with URI %s not found!", uri)));
    }

    @Override
    @Transactional
    public Candidate updateCandidate(UUID candidateId, UUID userId, CandidateModelRequest candidateModelRequest) {
        if (candidateModelRequest == null) {
            throw new RuntimeException("Candidate to update is null!");
        }
        Candidate candidate = this.getCandidateById(candidateId);

        // UPDATE issue fields
        if (this.candidateRepository.existsByUri(candidateModelRequest.getUri())) {
            Candidate candidateByURI = this.getCandidateByURI(candidateModelRequest.getUri());
            // NOT uri & name change
            if (!candidateByURI.getId().equals(candidate.getId())) {
                throw new EntityExistsException(String.format("Candidate uri %s already exist!", candidateModelRequest.getUri()));
            }
        }
        // ADD pattern language
        if (candidateModelRequest.getPatternLanguageId() != null && this.patternLanguageService.getPatternLanguageById(candidateModelRequest.getPatternLanguageId()) != null)
            candidate.setPatternLanguage(this.patternLanguageService.getPatternLanguageById(candidateModelRequest.getPatternLanguageId()));

        // UPDATE issue fields
        candidate.updateCandidate(candidateModelRequest);

        return this.candidateRepository.save(candidate);
    }

    @Override
    @Transactional
    public void deleteCandidate(UUID candidateId) {
        this.getCandidateById(candidateId);
        this.candidateRepository.deleteById(candidateId);
    }

    /**
     * Comment
     */
    @Override
    @Transactional
    public CandidateComment createComment(UUID candidateId, UUID userId, CommentModel commentModel) {
        Candidate candidate = this.getCandidateById(candidateId);
        UserEntity user = this.userService.getUserById(userId);

        CandidateComment comment = new CandidateComment(commentModel.getText(), candidate, user);
        return this.candidateCommentRepository.save(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public CandidateComment getCommentById(UUID candidateCommentId) {
        return this.candidateCommentRepository.findById(candidateCommentId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Candidate comment with ID %s not found!", candidateCommentId)));
    }

    @Override
    @Transactional
    public CandidateComment updateComment(UUID candidateId, UUID commentId, UUID userId, CommentModel commentModel) {
        if (commentModel == null)
            throw new RuntimeException("Candidate comment to update is null!");
        // Used to check if candidate actually exists
        CandidateComment candidateComment = this.authCandidateComment(candidateId, commentId, userId);

        // UPDATE issue comment
        candidateComment.updateComment(commentModel.getText());
        return this.candidateCommentRepository.save(candidateComment);
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteComment(UUID issueId, UUID commentId, UUID userId) {
        this.authCandidateComment(issueId, commentId, userId);
        this.candidateCommentRepository.deleteById(commentId);
        return ResponseEntity.noContent().build();
    }

    private CandidateComment authCandidateComment(UUID candidateId, UUID commentId, UUID userId) {
        CandidateComment candidateComment = this.getCommentById(commentId);
        // CORRECT Issue
        if (!candidateComment.getCandidate().equals(this.getCandidateById(candidateId)))
            throw new EntityNotFoundException(String.format("Candidate comment with id %s does not belong to candidate with id %s", commentId, candidateId));
        // CORRECT user
        if (!candidateComment.getUser().equals(this.userService.getUserById(userId)))
            throw new EntityNotFoundException(String.format("Candidate comment with id %s does not belong to user with id %s", commentId, userId));
        return candidateComment;
    }
}
