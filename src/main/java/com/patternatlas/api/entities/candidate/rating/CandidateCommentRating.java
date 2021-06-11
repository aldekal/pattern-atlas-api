package com.patternatlas.api.entities.candidate.rating;

import com.patternatlas.api.entities.candidate.CandidateComment;
import com.patternatlas.api.entities.user.UserEntity;
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
public class CandidateCommentRating {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private CandidateCommentRatingKey id;

    @ManyToOne
    @MapsId("candidateCommentId")
    @EqualsAndHashCode.Include
    private CandidateComment candidateComment;

    @ManyToOne
    @MapsId("userId")
    @EqualsAndHashCode.Include
    private UserEntity user;

    private int rating;

    public CandidateCommentRating(CandidateComment candidateComment, UserEntity user) {
        this.candidateComment = candidateComment;
        this.user = user;
        this.id = new CandidateCommentRatingKey(candidateComment.getId(), user.getId());
    }

    @Override
    public String toString() {
        return this.id.toString() + this.rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidateCommentRating)) return false;
        CandidateCommentRating that = (CandidateCommentRating) o;
        return Objects.equals(candidateComment.getText(), that.candidateComment.getText()) &&
                Objects.equals(user.getName(), that.user.getName()) &&
                Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(candidateComment.getText(), user.getName(), rating);
    }
}
