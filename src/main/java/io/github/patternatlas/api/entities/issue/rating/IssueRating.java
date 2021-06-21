package io.github.patternatlas.api.entities.issue.rating;

import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import io.github.patternatlas.api.entities.user.UserEntity;
import io.github.patternatlas.api.entities.issue.Issue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class IssueRating {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private IssueRatingKey id;

    @ManyToOne
    @MapsId("issueId")
    @EqualsAndHashCode.Include
    private Issue issue;

    @ManyToOne
    @MapsId("userId")
    @EqualsAndHashCode.Include
    private UserEntity user;

    private int rating;

    public IssueRating(Issue issue, UserEntity user) {
        this.issue = issue;
        this.user = user;
        this.id = new IssueRatingKey(issue.getId(), user.getId());
    }

    @Override
    public String toString() {
        return this.id.toString() + this.rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IssueRating)) return false;
        IssueRating that = (IssueRating) o;
        return Objects.equals(issue.getName(), that.issue.getName()) &&
                Objects.equals(user.getName(), that.user.getName()) &&
                Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issue.getName(), user.getName(), rating);
    }
}
