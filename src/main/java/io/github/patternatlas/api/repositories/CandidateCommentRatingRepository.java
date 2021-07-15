package io.github.patternatlas.api.repositories;

import io.github.patternatlas.api.entities.candidate.comment.CandidateCommentRating;
import io.github.patternatlas.api.entities.shared.CompositeKey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface CandidateCommentRatingRepository extends JpaRepository<CandidateCommentRating, CompositeKey> {

    boolean existsByIdAndRating(CompositeKey compositeKey, int rating);
}
