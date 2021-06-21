package io.github.patternatlas.api.entities.issue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.patternatlas.api.entities.issue.rating.IssueRating;
import io.github.patternatlas.api.entities.user.UserEntity;
import io.github.patternatlas.api.entities.EntityWithURI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Issue extends EntityWithURI {

    private String description;

    private int rating = 0;

    private String version = "0.1.0";

    @JsonIgnore
    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<IssueRating> userRating = new HashSet<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssueComment> comments = new ArrayList<>();

    public void addComment(IssueComment comment, UserEntity user) {
        comments.add(comment);
        comment.setIssue(this);
        comment.setUser(user);
    }

    public void updateComment(IssueComment updateComment) {
        ListIterator<IssueComment> commentIterator = comments.listIterator();
        while (commentIterator.hasNext()) {
            IssueComment next = commentIterator.next();
            if (next.getId().equals(updateComment.getId())) {
                commentIterator.set(updateComment);
                break;
            }
        }
    }

    public void removeComment(IssueComment comment) {
        comments.remove(comment);
        comment.setIssue(null);
        comment.setUser(null);
    }

    public String toString() {
        return this.getId().toString() + this.getDescription();
    }
}
