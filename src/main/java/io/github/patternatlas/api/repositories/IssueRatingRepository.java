package io.github.patternatlas.api.repositories;

import io.github.patternatlas.api.entities.issue.Issue;
import io.github.patternatlas.api.entities.issue.IssueRating;
import io.github.patternatlas.api.entities.shared.CompositeKey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

@RepositoryRestResource(exported = false)
public interface IssueRatingRepository extends JpaRepository<IssueRating, CompositeKey> {

    boolean existsByIdAndRating(CompositeKey compositeKey, int rating);
    Collection<IssueRating> findAllByIssue(Issue issue);
}