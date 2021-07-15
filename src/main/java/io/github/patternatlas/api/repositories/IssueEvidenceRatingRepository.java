package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.issue.evidence.IssueEvidenceRating;
import com.patternpedia.api.entities.shared.CompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueEvidenceRatingRepository extends JpaRepository<IssueEvidenceRating, CompositeKey> {

    boolean existsByIdAndRating(CompositeKey compositeKey, int rating);
}
