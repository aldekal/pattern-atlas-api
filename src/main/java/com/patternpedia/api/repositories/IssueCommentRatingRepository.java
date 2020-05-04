package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.issue.IssueComment;
import com.patternpedia.api.entities.rating.issue.comment.IssueCommentRating;
import com.patternpedia.api.entities.rating.issue.comment.IssueCommentRatingKey;
import com.patternpedia.api.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface IssueCommentRatingRepository extends JpaRepository<IssueCommentRating, IssueCommentRatingKey> {

    IssueCommentRating findByIssueCommentAndUser(IssueComment issueComment, UserEntity user);

}
