package com.patternatlas.api.entities.candidate.rating;

import com.patternatlas.api.entities.candidate.Candidate;
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
public class CandidateRating {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private CandidateRatingKey id;

    @ManyToOne
    @MapsId("candidateId")
    @EqualsAndHashCode.Include
    private Candidate candidate;

    @ManyToOne
    @MapsId("userId")
    @EqualsAndHashCode.Include
    private UserEntity user;

    private int rating;

    public CandidateRating(Candidate candidate, UserEntity user) {
        this.candidate = candidate;
        this.user = user;
        this.id = new CandidateRatingKey(candidate.getId(), user.getId());
    }

    @Override
    public String toString() {
        return this.id.toString() + this.rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidateRating)) return false;
        CandidateRating that = (CandidateRating) o;
        return Objects.equals(candidate.getName(), that.candidate.getName()) &&
                Objects.equals(user.getName(), that.user.getName()) &&
                Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(candidate.getName(), user.getName(), rating);
    }
}
