package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.candidate.CandidateRating;
import com.patternpedia.api.entities.shared.CompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface CandidateRatingRepository extends JpaRepository<CandidateRating, CompositeKey> {

    boolean existsByIdAndRating(CompositeKey compositeKey, int rating);
}
