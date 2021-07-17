package io.github.patternatlas.api.repositories;

import io.github.patternatlas.api.entities.issue.evidence.IssueEvidenceRating;
import io.github.patternatlas.api.entities.shared.CompositeKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueEvidenceRatingRepository extends JpaRepository<IssueEvidenceRating, CompositeKey> {

    boolean existsByIdAndRating(CompositeKey compositeKey, int rating);
}
