package com.patternpedia.api.service;

import com.patternpedia.api.entities.candidate.Candidate;
import com.patternpedia.api.entities.candidate.author.CandidateAuthor;
import com.patternpedia.api.entities.issue.Issue;
import com.patternpedia.api.entities.issue.author.IssueAuthor;
import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.rest.model.shared.AuthorModel;
import com.patternpedia.api.rest.model.shared.AuthorModelRequest;
import org.apache.catalina.User;

import java.util.Collection;
import java.util.UUID;

public interface AuthorService {

    IssueAuthor saveIssueAuthor(UUID userId, UUID issueId, AuthorModelRequest authorModelRequest);
    void deleteIssueAuthor(UUID userId, UUID issueId);

    CandidateAuthor saveCandidateAuthor(UUID userId, UUID candidateId, AuthorModelRequest authorModelRequest);
    void deleteCandidateAuthor(UUID userId, UUID candidateId);

//    Candidate updateCandidateAuthor(UUID candidateId, AuthorModelRequest authorModelRequest);
}
