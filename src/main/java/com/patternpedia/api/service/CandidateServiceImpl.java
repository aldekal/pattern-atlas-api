package com.patternpedia.api.service;

import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.candidate.Candidate;
import com.patternpedia.api.entities.candidate.CandidateComment;
import com.patternpedia.api.entities.issue.Issue;
import com.patternpedia.api.exception.*;
import com.patternpedia.api.repositories.*;
import com.patternpedia.api.util.RatingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CandidateServiceImpl implements CandidateService {

    private CandidateRepository candidateRepository;
    private CandidateRatingRepository candidateRatingRepository;
    private CandidateCommentRepository candidateCommentRepository;
    private CandidateCommentRatingRepository candidateCommentRatingRepository;
    private PatternLanguageService patternLanguageService;
    private UserService userService;
    private RatingHelper ratingHelper;

    Logger logger = LoggerFactory.getLogger(IssueServiceImpl.class);

    public CandidateServiceImpl(
            CandidateRepository candidateRepository,
            CandidateRatingRepository candidateRatingRepository,
            CandidateCommentRepository candidateCommentRepository,
            CandidateCommentRatingRepository candidateCommentRatingRepository,
            PatternLanguageService patternLanguageService,
            UserService userService
    ) {
        this.candidateRepository = candidateRepository;
        this.candidateRatingRepository = candidateRatingRepository;
        this.candidateCommentRepository = candidateCommentRepository;
        this.candidateCommentRatingRepository = candidateCommentRatingRepository;
        this.patternLanguageService = patternLanguageService;
        this.userService = userService;
        this.ratingHelper = new RatingHelper();
    }


    /**
     * CRUD Candidate
     */
    @Override
    @Transactional
    public Candidate createCandidate(Candidate candidate, UUID patternLanguageId) {
        if (null == candidate) {
            throw new NullCandidateException();
        }

        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);
        candidate.setPatternLanguage(patternLanguage);
        Candidate newCandidate = this.candidateRepository.save(candidate);
        logger.info(String.format("Create Candidate %s: ", newCandidate.toString()));
        return newCandidate;
    }

    @Override
    @Transactional
    public Candidate getCandidateById(UUID candidateId) {
        return this.candidateRepository.findById(candidateId)
                .orElseThrow(() -> new CandidateNotFoundException(candidateId));
    }

    @Override
    @Transactional
    public Candidate getCandidateByURI(String uri) {
        return this.candidateRepository.findByUri(uri)
                .orElseThrow(() -> new CandidateNotFoundException(String.format("Pattern with URI %s not found!", uri)));
    }

    @Override
    @Transactional
    public List<Candidate> getAllCandidates() {
        return this.candidateRepository.findAll();
    }

    @Override
    @Transactional
    public Candidate updateCandidate(Candidate candidate) {
        if (null == candidate) {
            throw new NullCandidateException();
        }
        if (!this.candidateRepository.existsById(candidate.getId())) {
            throw new CandidateNotFoundException(candidate.getId());
        }

        logger.info(String.format("Update Issue: %s", candidate.toString()));
        return this.candidateRepository.save(candidate);
    }

    @Override
    @Transactional
    public void deleteCandidate(UUID candidateId) {
        Candidate candidate = this.getCandidateById(candidateId);
        if (null == candidate) {
            throw new NullCandidateException();
        }

        this.candidateRepository.deleteById(candidateId);
    }

    /** */
    @Override
    public Candidate userRating(UUID candidateId, UUID userId, String rating) {
        return null;
    }

    @Override
    public Candidate createComment(UUID candidateId, UUID userId, CandidateComment candidateComment) {
        return null;
    }

    @Override
    public CandidateComment getCommentById(UUID candidateCommentId) {
        return null;
    }

    @Override
    public CandidateComment updateComment(CandidateComment candidateComment) {
        return null;
    }

    @Override
    public Candidate commentUserRating(UUID candidateCommentId, UUID userId, String rating) {
        return null;
    }
}
