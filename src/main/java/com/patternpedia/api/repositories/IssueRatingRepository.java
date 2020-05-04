package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.issue.Issue;
import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.entities.rating.issue.IssueRatingKey;
import com.patternpedia.api.entities.rating.issue.IssueRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface IssueRatingRepository extends JpaRepository<IssueRating, IssueRatingKey> {

    List<IssueRating> findAllByIssue(Issue issue);

    List<UserEntity> findAllByUser(UserEntity user);

    IssueRating findByIssueAndUser(Issue issue, UserEntity user);

//    RatingPatternEvolution findById(RatingKey ratingKey);
}
