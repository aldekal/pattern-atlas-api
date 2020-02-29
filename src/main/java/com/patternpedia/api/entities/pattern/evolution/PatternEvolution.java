package com.patternpedia.api.entities.pattern.evolution;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.patternpedia.api.entities.EntityWithURI;
import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.entities.rating.RatingPatternEvolution;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PatternEvolution extends EntityWithURI {

    private String description;

    private int rating = 0;

    private String version = "0.1.0";

    @JsonIgnore
    @OneToMany(mappedBy = "patternEvolution", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RatingPatternEvolution> userRating = new HashSet<>();

//    @JsonIgnore()
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(mappedBy = "patternEvolution", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentPatternEvolution> comments = new ArrayList<>();


    public void addComment(CommentPatternEvolution comment, UserEntity user) {
        comments.add(comment);
        comment.setPatternEvolution(this);
        comment.setUser(user);
    }

    public void removeComment(CommentPatternEvolution comment) {
        comments.remove(comment);
        comment.setPatternEvolution(null);
        comment.setUser(null);
    }

    public String toString() {
        return this.getId().toString() + userRating.toString();
    }

}
