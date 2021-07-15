package io.github.patternatlas.api.repositories;

import io.github.patternatlas.api.entities.candidate.CandidateRating;
import io.github.patternatlas.api.entities.shared.CompositeKey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface CandidateRatingRepository extends JpaRepository<CandidateRating, CompositeKey> {

    boolean existsById(CompositeKey compositeKey);
    boolean existsByIdAndReadability(CompositeKey compositeKey, int rating);
    boolean existsByIdAndUnderstandability(CompositeKey compositeKey, int rating);
    boolean existsByIdAndAppropriateness(CompositeKey compositeKey, int rating);
}