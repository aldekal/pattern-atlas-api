package io.github.patternatlas.api.entities.candidate.evidence;

import io.github.patternatlas.api.entities.shared.CompositeKey;
import io.github.patternatlas.api.entities.user.UserEntity;

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
public class CandidateEvidenceRating {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private CompositeKey id;

    @ManyToOne
    @MapsId("entityId")
    @EqualsAndHashCode.Include
    private CandidateEvidence candidateEvidence;

    @ManyToOne
    @MapsId("userId")
    @EqualsAndHashCode.Include
    private UserEntity user;

    private int rating;

    public CandidateEvidenceRating(CandidateEvidence candidateEvidence, UserEntity user) {
        this.candidateEvidence = candidateEvidence;
        this.user = user;
        this.id = new CompositeKey(candidateEvidence.getId(), user.getId());
    }

    public CandidateEvidenceRating(CandidateEvidence candidateEvidence, UserEntity user, int rating) {
        this(candidateEvidence, user);
        this.rating = rating;
    }

    @Override
    public String toString() {
        return this.id.toString() + this.rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidateEvidenceRating)) return false;
        CandidateEvidenceRating that = (CandidateEvidenceRating) o;
        return Objects.equals(candidateEvidence.getTitle(), that.candidateEvidence.getTitle()) &&
                Objects.equals(user.getName(), that.user.getName()) &&
                Objects.equals(rating, that.candidateEvidence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(candidateEvidence.getTitle(), user.getName(), rating);
    }
}

