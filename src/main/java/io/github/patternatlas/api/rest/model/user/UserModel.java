package io.github.patternatlas.api.rest.model.user;

import io.github.patternatlas.api.entities.user.UserEntity;
import io.github.patternatlas.api.rest.model.candidate.CandidateModel;
import io.github.patternatlas.api.rest.model.issue.IssueModel;
import io.github.patternatlas.api.rest.model.shared.CommentModel;
import io.github.patternatlas.api.rest.model.shared.EvidenceModel;
import io.github.patternatlas.api.rest.model.shared.RatingModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class UserModel {

    private UUID id;
    private String role;
    private String email;
    private String name;
    private List<IssueModel> issues;
    private List<CommentModel> issueComments;
    private List<IssueModel> issueRatings;
    private List<EvidenceModel> issueEvidences;
    private List<CandidateModel> candidates;
    private List<CommentModel> candidateComments;
    private List<CandidateModel> candidatesRatings;
    private List<EvidenceModel> candidatesEvidences;
    private List<String> patterns = new ArrayList<>();
    private List<String> patternComments = new ArrayList<>();
    private List<String> patternRatings = new ArrayList<>();
    private List<String> patternEvidences = new ArrayList<>();

    public UserModel(UserEntity user) {
        this.id = user.getId();
        this.role = user.getRole().getName();
        this.email = user.getEmail();
        this.name = user.getName();
        // ISSUE
        this.issues = user.getIssues().stream().map(issueAuthor -> new IssueModel(issueAuthor.getIssue())).collect(Collectors.toList());
        this.issueComments = user.getIssueComments().stream().map(issueComment -> new CommentModel(issueComment)).collect(Collectors.toList());
        this.issueRatings = user.getIssueRatings().stream().map((issueRating -> new IssueModel(issueRating.getIssue()))).collect(Collectors.toList());
        this.issueEvidences = user.getIssueEvidence().stream().map(issueEvidence -> new EvidenceModel(issueEvidence)).collect(Collectors.toList());
        // CANDIDATE
        this.candidates = user.getCandidates().stream().map(candidateAuthor -> new CandidateModel(candidateAuthor.getCandidate())).collect(Collectors.toList());
        this.candidateComments = user.getCandidateComments().stream().map(candidateComment -> new CommentModel(candidateComment)).collect(Collectors.toList());
        this.candidatesRatings = user.getCandidateRatings().stream().map((candidateRating -> new CandidateModel(candidateRating.getCandidate()))).collect(Collectors.toList());
        this.candidatesEvidences = user.getCandidateEvidence().stream().map(candidateEvidence -> new EvidenceModel(candidateEvidence)).collect(Collectors.toList());
    }
}
