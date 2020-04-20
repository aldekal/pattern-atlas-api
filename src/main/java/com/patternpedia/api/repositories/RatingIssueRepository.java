package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.issue.Issue;
import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.entities.rating.RatingKey;
import com.patternpedia.api.entities.rating.RatingIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface RatingIssueRepository extends JpaRepository<RatingIssue, RatingKey> {

    List<RatingIssue> findAllByIssue(Issue issue);

    List<UserEntity> findAllByUser(UserEntity user);

    RatingIssue findByIssueAndUser(Issue issue, UserEntity user);

//    RatingPatternEvolution findById(RatingKey ratingKey);
}
