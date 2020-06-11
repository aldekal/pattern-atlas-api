package com.patternpedia.api.service;

import com.patternpedia.api.entities.candidate.Candidate;
import com.patternpedia.api.entities.candidate.author.CandidateAuthor;
import com.patternpedia.api.entities.issue.Issue;
import com.patternpedia.api.entities.issue.IssueRating;
import com.patternpedia.api.entities.issue.author.IssueAuthor;
import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.repositories.CandidateAuthorRepository;
import com.patternpedia.api.repositories.IssueAuthorRepository;
import com.patternpedia.api.rest.model.shared.AuthorModel;
import com.patternpedia.api.rest.model.shared.AuthorModelRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);

    private IssueAuthorRepository issueAuthorRepository;
    private IssueService issueService;
    private CandidateAuthorRepository candidateAuthorRepository;
    private CandidateService candidateService;
    private UserService userService;

    public AuthorServiceImpl(
            IssueAuthorRepository issueAuthorRepository,
            IssueService issueService,
            CandidateAuthorRepository candidateAuthorRepository,
            CandidateService candidateService,
            UserService userService
    ) {
        this.issueAuthorRepository = issueAuthorRepository;
        this.issueService = issueService;
        this.candidateAuthorRepository = candidateAuthorRepository;
        this.candidateService = candidateService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public IssueAuthor saveIssueAuthor(UUID userId, UUID issueId, AuthorModelRequest authorModelRequest) {
        Issue issue = issueService.getIssueById(issueId);
        UserEntity user = userService.getUserById(userId);
        IssueAuthor issueAuthor = new IssueAuthor(issue, user, authorModelRequest.getAuthorRole());
        return this.issueAuthorRepository.save(issueAuthor);
    }

    @Override
    @Transactional
    public void deleteIssueAuthor(UUID userId, UUID issueId) {
        Issue issue = this.issueService.getIssueById(issueId);
        UserEntity user = this.userService.getUserById(userId);
        IssueAuthor issueAuthor = new IssueAuthor(issue, user);
        if (this.issueAuthorRepository.existsById(issueAuthor.getId())) {
            this.issueAuthorRepository.deleteById(issueAuthor.getId());
        }
    }

    @Override
    @Transactional
    public CandidateAuthor saveCandidateAuthor(UUID userId, UUID candidateId, AuthorModelRequest authorModelRequest) {
        Candidate candidate = this.candidateService.getCandidateById(candidateId);
        UserEntity user = this.userService.getUserById(userId);
        CandidateAuthor candidateAuthor = new CandidateAuthor(candidate, user, authorModelRequest.getAuthorRole());
        return this.candidateAuthorRepository.save(candidateAuthor);
    }

    @Override
    @Transactional
    public void deleteCandidateAuthor(UUID userId, UUID candidateId) {
        Candidate candidate = this.candidateService.getCandidateById(candidateId);
        UserEntity user = this.userService.getUserById(userId);
        CandidateAuthor candidateAuthor = new CandidateAuthor(candidate, user);
        if (this.candidateAuthorRepository.existsById(candidateAuthor.getId())) {
            this.candidateAuthorRepository.deleteById(candidateAuthor.getId());
        }
    }

}

