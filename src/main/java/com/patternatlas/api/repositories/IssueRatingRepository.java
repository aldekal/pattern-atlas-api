package com.patternatlas.api.repositories;

import com.patternatlas.api.entities.issue.Issue;
import com.patternatlas.api.entities.user.UserEntity;
import com.patternatlas.api.entities.issue.rating.IssueRatingKey;
import com.patternatlas.api.entities.issue.rating.IssueRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface IssueRatingRepository extends JpaRepository<IssueRating, IssueRatingKey> {

    List<IssueRating> findAllByIssue(Issue issue);

    List<UserEntity> findAllByUser(UserEntity user);

    IssueRating findByIssueAndUser(Issue issue, UserEntity user);
}
