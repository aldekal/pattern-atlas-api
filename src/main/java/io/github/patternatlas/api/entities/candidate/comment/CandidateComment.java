package io.github.patternatlas.api.entities.candidate.comment;

import io.github.patternatlas.api.entities.candidate.Candidate;
import io.github.patternatlas.api.entities.candidate.comment.CandidateCommentRating;
import io.github.patternatlas.api.entities.shared.Comment;
import io.github.patternatlas.api.entities.user.UserEntity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CandidateComment extends Comment implements Serializable {

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private Candidate candidate;

    @JsonIgnore
    @OneToMany(mappedBy = "candidateComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CandidateCommentRating> userRating = new HashSet<>();

    private int rating = 0;

    public CandidateComment(String text, Candidate candidate, UserEntity user) {
        super(text, user);
        this.candidate = candidate;
    }

    public void updateComment(String text) {
        this.setText(text);
    }
}
