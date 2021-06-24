package io.github.patternatlas.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.patternatlas.api.entities.issue.rating.IssueRating;
import io.github.patternatlas.api.entities.issue.rating.IssueRatingKey;
import io.github.patternatlas.api.entities.user.UserEntity;
import io.github.patternatlas.api.entities.issue.Issue;

@RepositoryRestResource(exported = false)
public interface IssueRatingRepository extends JpaRepository<IssueRating, IssueRatingKey> {

    List<IssueRating> findAllByIssue(Issue issue);

    List<UserEntity> findAllByUser(UserEntity user);

    IssueRating findByIssueAndUser(Issue issue, UserEntity user);
}
