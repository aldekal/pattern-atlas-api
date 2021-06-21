package io.github.ust.quantil.patternatlas.api.entities.candidate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.ust.quantil.patternatlas.api.entities.EntityWithURI;
import io.github.ust.quantil.patternatlas.api.entities.PatternLanguage;
import io.github.ust.quantil.patternatlas.api.entities.candidate.rating.CandidateRating;
import io.github.ust.quantil.patternatlas.api.entities.user.UserEntity;
import io.github.ust.quantil.patternatlas.api.rest.model.CandidateModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Candidate extends EntityWithURI {

    private String iconUrl;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private PatternLanguage patternLanguage;

//    @Type(type = "jsonb")
//    @Column(columnDefinition = "jsonb")
//    @NotNull
    private String content;

    private int rating = 0;

    private String version = "0.1.0";

    @JsonIgnore
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CandidateRating> userRating = new HashSet<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CandidateComment> comments = new ArrayList<>();

    public Candidate (CandidateModel candidateModel) {
        this.setId(candidateModel.getId());
        this.setUri(candidateModel.getUri());
        this.setName(candidateModel.getName());
        this.setIconUrl(candidateModel.getIconUrl());
       //patternLanguage
        this.setContent(candidateModel.getContent());
        this.setVersion(candidateModel.getVersion());
    }


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
