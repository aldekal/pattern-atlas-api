package io.github.patternatlas.api.repositories;

import io.github.patternatlas.api.entities.candidate.evidence.CandidateEvidenceRating;
import io.github.patternatlas.api.entities.shared.CompositeKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateEvidenceRatingRepository extends JpaRepository<CandidateEvidenceRating, CompositeKey> {

    boolean existsByIdAndRating(CompositeKey compositeKey, int rating);
}
