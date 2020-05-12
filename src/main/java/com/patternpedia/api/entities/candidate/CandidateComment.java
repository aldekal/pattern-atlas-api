package com.patternpedia.api.entities.candidate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.patternpedia.api.entities.candidate.rating.CandidateCommentRating;
import com.patternpedia.api.entities.issue.Issue;
import com.patternpedia.api.entities.issue.IssueComment;
import com.patternpedia.api.entities.issue.rating.IssueCommentRating;
import com.patternpedia.api.entities.shared.Comment;
import com.patternpedia.api.entities.user.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class CandidateComment extends Comment implements Serializable {

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private Candidate candidate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ToString.Exclude
    @ManyToOne
    private UserEntity user;

    @JsonIgnore
    @OneToMany(mappedBy = "candidateComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CandidateCommentRating> userRating = new HashSet<>();

    public CandidateComment(String text) {
        super(text);
    }
}
