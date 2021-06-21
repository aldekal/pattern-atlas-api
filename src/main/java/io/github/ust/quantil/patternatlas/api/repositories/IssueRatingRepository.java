package io.github.ust.quantil.patternatlas.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.ust.quantil.patternatlas.api.entities.issue.Issue;
import io.github.ust.quantil.patternatlas.api.entities.issue.rating.IssueRating;
import io.github.ust.quantil.patternatlas.api.entities.issue.rating.IssueRatingKey;
import io.github.ust.quantil.patternatlas.api.entities.user.UserEntity;

@RepositoryRestResource(exported = false)
public interface IssueRatingRepository extends JpaRepository<IssueRating, IssueRatingKey> {

    List<IssueRating> findAllByIssue(Issue issue);

    List<UserEntity> findAllByUser(UserEntity user);

    IssueRating findByIssueAndUser(Issue issue, UserEntity user);
}
