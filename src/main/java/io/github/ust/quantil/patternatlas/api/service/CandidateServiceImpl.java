package io.github.ust.quantil.patternatlas.api.service;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.ust.quantil.patternatlas.api.entities.PatternLanguage;
import io.github.ust.quantil.patternatlas.api.entities.candidate.Candidate;
import io.github.ust.quantil.patternatlas.api.entities.candidate.CandidateComment;
import io.github.ust.quantil.patternatlas.api.exception.CandidateNotFoundException;
import io.github.ust.quantil.patternatlas.api.exception.NullCandidateException;
import io.github.ust.quantil.patternatlas.api.repositories.CandidateCommentRatingRepository;
import io.github.ust.quantil.patternatlas.api.repositories.CandidateCommentRepository;
import io.github.ust.quantil.patternatlas.api.repositories.CandidateRatingRepository;
import io.github.ust.quantil.patternatlas.api.repositories.CandidateRepository;
import io.github.ust.quantil.patternatlas.api.rest.model.CandidateModel;
import io.github.ust.quantil.patternatlas.api.util.RatingHelper;

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
    public Candidate createCandidate(CandidateModel candidateModel) {
        Candidate candidate = new Candidate(candidateModel);

        if (null == candidate) {
            throw new NullCandidateException();
        }

        if (candidateModel.getPatternLanguageId() != null) {
            PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(candidateModel.getPatternLanguageId());
            candidate.setPatternLanguage(patternLanguage);
        } else {
            candidate.setPatternLanguage(null);
        }

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

    /**
     *
     */
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
