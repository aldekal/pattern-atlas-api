package com.patternpedia.api.entities.rating.issue;

import com.patternpedia.api.entities.issue.Issue;
import com.patternpedia.api.entities.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
//@EqualsAndHashCode
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
