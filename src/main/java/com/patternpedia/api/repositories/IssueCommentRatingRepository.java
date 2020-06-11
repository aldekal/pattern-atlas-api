package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.issue.comment.IssueComment;
import com.patternpedia.api.entities.issue.comment.IssueCommentRating;
import com.patternpedia.api.entities.shared.CompositeKey;
import com.patternpedia.api.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface IssueCommentRatingRepository extends JpaRepository<IssueCommentRating, CompositeKey> {

    IssueCommentRating findByIssueCommentAndUser(IssueComment issueComment, UserEntity user);

    boolean existsByIdAndRating(CompositeKey compositeKey, int rating);

}
