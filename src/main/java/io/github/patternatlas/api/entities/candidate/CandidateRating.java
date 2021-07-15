package io.github.patternatlas.api.entities.candidate;

import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import io.github.patternatlas.api.entities.user.UserEntity;
import io.github.patternatlas.api.entities.candidate.Candidate;
import io.github.patternatlas.api.entities.shared.CompositeKey;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
public class CandidateRating {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private CompositeKey id;

    @ManyToOne
    @MapsId("entityId")
    @EqualsAndHashCode.Include
    private Candidate candidate;

    @ManyToOne
    @MapsId("userId")
    @EqualsAndHashCode.Include
    private UserEntity user;

    private int readability;
    private int understandability;
    private int appropriateness;

    public CandidateRating(Candidate candidate, UserEntity user) {
        this.candidate = candidate;
        this.user = user;
        this.id = new CompositeKey(candidate.getId(), user.getId());
    }

    public CandidateRating(Candidate candidate, UserEntity user, int readability, int understandability, int appropriateness) {
        this(candidate, user);
        this.readability = readability;
        this.understandability = understandability;
        this.appropriateness = appropriateness;
    }

    @Override
    public String toString() {
        return this.id.toString() + this.readability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidateRating)) return false;
        CandidateRating that = (CandidateRating) o;
        return Objects.equals(candidate.getName(), that.candidate.getName()) &&
                Objects.equals(user.getName(), that.user.getName()) &&
                Objects.equals(readability, that.readability) &&
                Objects.equals(understandability, that.understandability) &&
                Objects.equals(appropriateness, that.appropriateness);
    }

    @Override
    public int hashCode() {
        return Objects.hash(candidate.getName(), user.getName(), readability);
    }
}
