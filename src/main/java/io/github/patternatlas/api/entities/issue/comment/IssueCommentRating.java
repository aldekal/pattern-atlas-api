package io.github.patternatlas.api.entities.issue.comment;

import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import io.github.patternatlas.api.entities.user.UserEntity;
import io.github.patternatlas.api.entities.issue.comment.IssueComment;
import io.github.patternatlas.api.entities.shared.CompositeKey;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class IssueCommentRating {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private CompositeKey id;

    @ManyToOne
    @MapsId("entityId")
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
        this.id = new CompositeKey(issueComment.getId(), user.getId());
    }

    public IssueCommentRating(IssueComment issueComment, UserEntity user, int rating) {
        this(issueComment, user);
        this.rating = rating;
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

