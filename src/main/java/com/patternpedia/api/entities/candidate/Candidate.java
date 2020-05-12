package com.patternpedia.api.entities.candidate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.patternpedia.api.entities.EntityWithURI;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.candidate.rating.CandidateRating;
import com.patternpedia.api.entities.user.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Candidate extends EntityWithURI {

    private String iconUrl;

//    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private PatternLanguage patternLanguage;

//    @JsonIgnore
//    @OneToMany(mappedBy = "pattern", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<PatternViewPattern> patternViews = new ArrayList<>();

//    @Type(type = "jsonb")
//    @Column(columnDefinition = "jsonb")
//    @NotNull
    private String content;

    private int rating = 0;

    private String version = "0.1.0";

//    private String patternLanguage;

    @JsonIgnore
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CandidateRating> userRating = new HashSet<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CandidateComment> comments = new ArrayList<>();


    public void addComment(CandidateComment comment, UserEntity user) {
        comments.add(comment);
        comment.setCandidate(this);
        comment.setUser(user);
    }

    public void updateComment(CandidateComment updateComment) {
        ListIterator<CandidateComment> commentIterator = comments.listIterator();
        while (commentIterator.hasNext()) {
            CandidateComment next = commentIterator.next();
            if (next.getId().equals(updateComment.getId())) {
                commentIterator.set(updateComment);
                break;
            }
        }
    }

    public void removeComment(CandidateComment comment) {
        comments.remove(comment);
        comment.setCandidate(null);
        comment.setUser(null);
    }

    public String toString() {
        return this.getId().toString();
    }
}
