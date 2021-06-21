package io.github.ust.quantil.patternatlas.api.entities.candidate;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.ust.quantil.patternatlas.api.entities.candidate.rating.CandidateCommentRating;
import io.github.ust.quantil.patternatlas.api.entities.shared.Comment;
import io.github.ust.quantil.patternatlas.api.entities.user.UserEntity;
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
