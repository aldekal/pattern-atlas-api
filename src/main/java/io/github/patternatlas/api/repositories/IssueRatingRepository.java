package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.issue.Issue;
import com.patternpedia.api.entities.issue.IssueRating;
import com.patternpedia.api.entities.shared.CompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

@RepositoryRestResource(exported = false)
public interface IssueRatingRepository extends JpaRepository<IssueRating, CompositeKey> {

    boolean existsByIdAndRating(CompositeKey compositeKey, int rating);
    Collection<IssueRating> findAllByIssue(Issue issue);
}
