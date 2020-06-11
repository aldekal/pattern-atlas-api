package com.patternpedia.api.rest.model.shared;

import com.patternpedia.api.entities.candidate.comment.CandidateComment;
import com.patternpedia.api.entities.candidate.comment.CandidateCommentRating;
import com.patternpedia.api.entities.issue.IssueRating;
import com.patternpedia.api.entities.issue.comment.IssueComment;
import com.patternpedia.api.entities.issue.comment.IssueCommentRating;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class CommentModel {

    private UUID id;
    private UUID userId;
    private String userName;
    private String text;
    private Collection<UUID> upVotes = new ArrayList<>();
    private Collection<UUID> downVotes = new ArrayList<>();

    /** For Issue Comments*/
    public CommentModel(IssueComment issueComment) {
        this.id     = issueComment.getId();
        this.userId = issueComment.getUser().getId();
        this.userName = issueComment.getUser().getName();
        this.text   = issueComment.getText();
        for (IssueCommentRating issueRating: issueComment.getUserRating()) {
            if (issueRating.getRating() == 1)
                this.upVotes.add(issueRating.getUser().getId());
            if (issueRating.getRating() == -1)
                this.downVotes.add(issueRating.getUser().getId());
        }
    }
    public static CommentModel from(IssueComment issueComment) { return new CommentModel(issueComment); }

    /** For Candidate Comments*/
    public CommentModel(CandidateComment candidateComment) {
        this.id     = candidateComment.getId();
        this.userId = candidateComment.getUser().getId();
        this.userName = candidateComment.getUser().getName();
        this.text   = candidateComment.getText();
        for (CandidateCommentRating candidateCommentRating: candidateComment.getUserRating()) {
            if (candidateCommentRating.getRating() == 1)
                this.upVotes.add(candidateCommentRating.getUser().getId());
            if (candidateCommentRating.getRating() == -1)
                this.downVotes.add(candidateCommentRating.getUser().getId());
        }
    }
    public static CommentModel from(CandidateComment candidateComment) { return new CommentModel(candidateComment); }
}
