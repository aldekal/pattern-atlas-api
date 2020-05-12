package com.patternpedia.api.entities.issue.rating;

import com.patternpedia.api.entities.issue.IssueComment;
import com.patternpedia.api.entities.user.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
public class IssueCommentRating {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private IssueCommentRatingKey id;

    @ManyToOne
    @MapsId("issueCommentId")
    @EqualsAndHashCode.Include
    private IssueComment issueComment;

    @ManyToOne
    @MapsId("userId")
    @EqualsAndHashCode.Include
    private UserEntity user;

    private int rating;

    public IssueCommentRating(IssueComment issueComment, UserEntity user) {
        this.issueComment = issueComment;
        this.user = user;
        this.id = new IssueCommentRatingKey(issueComment.getId(), user.getId());
    }

    @Override
    public String toString() {
        return this.id.toString() + this.rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IssueCommentRating)) return false;
        IssueCommentRating that = (IssueCommentRating) o;
        return Objects.equals(issueComment.getText(), that.issueComment.getText()) &&
                Objects.equals(user.getName(), that.user.getName()) &&
                Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issueComment.getText(), user.getName(), rating);
    }
}

