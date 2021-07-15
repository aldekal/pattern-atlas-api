package io.github.patternatlas.api.entities.issue;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

import io.github.patternatlas.api.entities.user.UserEntity;
import io.github.patternatlas.api.entities.issue.Issue;
import io.github.patternatlas.api.entities.shared.CompositeKey;

@Entity
@Data
@NoArgsConstructor
public class IssueRating {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private CompositeKey id;

    @ManyToOne
    @MapsId("entityId")
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
        this.id = new CompositeKey(issue.getId(), user.getId());
    }

    public IssueRating(Issue issue, UserEntity user, int rating) {
       this(issue, user);
       this.rating = rating;
    }

    public void update(int rating) {
        this.rating = rating;
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
        return Objects.equals(issue.getId(), that.issue.getId()) &&
                Objects.equals(user.getId(), that.user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(issue.getName(), user.getName(), rating);
    }
}
