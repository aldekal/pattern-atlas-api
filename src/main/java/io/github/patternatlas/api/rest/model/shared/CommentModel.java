package io.github.patternatlas.api.rest.model.shared;

import io.github.patternatlas.api.entities.candidate.comment.CandidateComment;
import io.github.patternatlas.api.entities.candidate.comment.CandidateCommentRating;
import io.github.patternatlas.api.entities.issue.IssueRating;
import io.github.patternatlas.api.entities.issue.comment.IssueComment;
import io.github.patternatlas.api.entities.issue.comment.IssueCommentRating;
import io.github.patternatlas.api.entities.shared.Comment;
import io.github.patternatlas.api.entities.user.UserEntity;

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
    private int rating;
    private Collection<UUID> upVotes = new ArrayList<>();
    private Collection<UUID> downVotes = new ArrayList<>();

    private void initialize(Comment comment) {
        this.id = comment.getId();
        this.userId = comment.getUser().getId();
        this.userName = comment.getUser().getName();
        this.text = comment.getText();
        this.rating = 0;
    }

    private void updateRatingInformation(UserEntity user, int rating) {
        if (rating == 1) {
            this.upVotes.add(user.getId());
            this.rating = this.rating + 1;
        }
        if (rating == -1) {
            this.downVotes.add(user.getId());
            this.rating = this.rating - 1;
        }
    }

    /**
     * For Issue Comments
     */
    public CommentModel(IssueComment issueComment) {
        initialize(issueComment);
        for (IssueCommentRating issueRating : issueComment.getUserRating()) {
            updateRatingInformation(issueRating.getUser(), issueRating.getRating());
        }
    }

    public static CommentModel from(IssueComment issueComment) {
        return new CommentModel(issueComment);
    }

    /**
     * For Candidate Comments
     */
    public CommentModel(CandidateComment candidateComment) {
        initialize(candidateComment);
        for (CandidateCommentRating issueRating : candidateComment.getUserRating()) {
            updateRatingInformation(issueRating.getUser(), issueRating.getRating());
        }
    }

    public static CommentModel from(CandidateComment candidateComment) {
        return new CommentModel(candidateComment);
    }

    public int compareTo(CommentModel o)
    {
        return(rating - o.rating);
    }
}
