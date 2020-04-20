package com.patternpedia.api.entities.issue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.patternpedia.api.entities.EntityWithURI;
import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.entities.rating.RatingIssue;
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
    private Set<RatingIssue> userRating = new HashSet<>();

//    @JsonIgnore()
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentIssue> comments = new ArrayList<>();


    public void addComment(CommentIssue comment, UserEntity user) {
        comments.add(comment);
        comment.setIssue(this);
        comment.setUser(user);
    }

    public void removeComment(CommentIssue comment) {
        comments.remove(comment);
        comment.setIssue(null);
        comment.setUser(null);
    }

    public String toString() {
        return this.getId().toString() + userRating.toString();
    }

}
