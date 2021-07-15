package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.candidate.evidence.CandidateEvidenceRating;
import com.patternpedia.api.entities.shared.CompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateEvidenceRatingRepository extends JpaRepository<CandidateEvidenceRating, CompositeKey> {

    boolean existsByIdAndRating(CompositeKey compositeKey, int rating);
}
