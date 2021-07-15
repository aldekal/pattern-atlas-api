package com.patternpedia.api.entities.issue.evidence;

import com.patternpedia.api.entities.issue.Issue;
import com.patternpedia.api.entities.issue.comment.IssueComment;
import com.patternpedia.api.entities.issue.comment.IssueCommentRating;
import com.patternpedia.api.entities.shared.CompositeKey;
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
public class IssueEvidenceRating {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private CompositeKey id;

    @ManyToOne
    @MapsId("entityId")
    @EqualsAndHashCode.Include
    private IssueEvidence issueEvidence;

    @ManyToOne
    @MapsId("userId")
    @EqualsAndHashCode.Include
    private UserEntity user;

    private int rating;

    public IssueEvidenceRating(IssueEvidence issueEvidence, UserEntity user) {
        this.issueEvidence = issueEvidence;
        this.user = user;
        this.id = new CompositeKey(issueEvidence.getId(), user.getId());
    }

    public IssueEvidenceRating(IssueEvidence issueEvidence, UserEntity user, int rating) {
        this(issueEvidence, user);
        this.rating = rating;
    }

    @Override
    public String toString() {
        return this.id.toString() + this.rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IssueEvidenceRating)) return false;
        IssueEvidenceRating that = (IssueEvidenceRating) o;
        return Objects.equals(issueEvidence.getTitle(), that.issueEvidence.getTitle()) &&
                Objects.equals(user.getName(), that.user.getName()) &&
                Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issueEvidence.getTitle(), user.getName(), rating);
    }
}
