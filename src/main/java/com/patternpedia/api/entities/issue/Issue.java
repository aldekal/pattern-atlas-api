package com.patternpedia.api.entities.issue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.patternpedia.api.entities.EntityWithURI;
import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.entities.issue.rating.IssueRating;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

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
