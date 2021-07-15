package com.patternpedia.api.rest.model.shared;

import com.patternpedia.api.entities.candidate.comment.CandidateComment;
import com.patternpedia.api.entities.candidate.comment.CandidateCommentRating;
import com.patternpedia.api.entities.candidate.evidence.CandidateEvidence;
import com.patternpedia.api.entities.candidate.evidence.CandidateEvidenceRating;
import com.patternpedia.api.entities.issue.comment.IssueComment;
import com.patternpedia.api.entities.issue.comment.IssueCommentRating;
import com.patternpedia.api.entities.issue.evidence.IssueEvidence;
import com.patternpedia.api.entities.issue.evidence.IssueEvidenceRating;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class EvidenceModel {

    private UUID id;
    private UUID userId;
    private String userName;
    private String title;
    private String context;
    private String type;
    private Boolean supporting;
    private String source;
    private int rating;
    private Collection<UUID> upVotes = new ArrayList<>();
    private Collection<UUID> downVotes = new ArrayList<>();

    /**
     * For Issue Comments
     */
    public EvidenceModel(IssueEvidence issueEvidence) {
        this.id = issueEvidence.getId();
        this.userId = issueEvidence.getUser().getId();
        this.userName = issueEvidence.getUser().getName();
        this.title = issueEvidence.getTitle();
        this.context = issueEvidence.getContext();
        this.type = issueEvidence.getType();
        this.supporting = issueEvidence.getSupporting();
        this.source = issueEvidence.getSource();
        this.rating = 0;
        for (IssueEvidenceRating issueRating : issueEvidence.getUserRating()) {
            if (issueRating.getRating() == 1) {
                this.upVotes.add(issueRating.getUser().getId());
                this.rating = this.rating + 1;
            }
            if (issueRating.getRating() == -1) {
                this.downVotes.add(issueRating.getUser().getId());
                this.rating = this.rating - 1;
            }
        }
    }

    public static EvidenceModel from(IssueEvidence issueEvidence) {
        return new EvidenceModel(issueEvidence);
    }

    /**
     * For Candidate Evidence
     */
    public EvidenceModel(CandidateEvidence candidateEvidence) {
        this.id = candidateEvidence.getId();
        this.userId = candidateEvidence.getUser().getId();
        this.userName = candidateEvidence.getUser().getName();
        this.title = candidateEvidence.getTitle();
        this.context = candidateEvidence.getContext();
        this.type = candidateEvidence.getType();
        this.supporting = candidateEvidence.getSupporting();
        this.source = candidateEvidence.getSource();
        this.rating = 0;
        for (CandidateEvidenceRating candidateEvidenceRating : candidateEvidence.getUserRating()) {
            if (candidateEvidenceRating.getRating() == 1) {
                this.upVotes.add(candidateEvidenceRating.getUser().getId());
                this.rating = this.rating + 1;
            }
            if (candidateEvidenceRating.getRating() == -1) {
                this.downVotes.add(candidateEvidenceRating.getUser().getId());
                this.rating = this.rating - 1;
            }
        }
    }

    public static EvidenceModel from(CandidateEvidence candidateEvidence) {
        return new EvidenceModel(candidateEvidence);
    }
}
