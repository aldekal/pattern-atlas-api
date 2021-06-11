package com.patternatlas.api.repositories;

import com.patternatlas.api.entities.issue.IssueComment;
import com.patternatlas.api.entities.issue.rating.IssueCommentRating;
import com.patternatlas.api.entities.issue.rating.IssueCommentRatingKey;
import com.patternatlas.api.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface IssueCommentRatingRepository extends JpaRepository<IssueCommentRating, IssueCommentRatingKey> {

    IssueCommentRating findByIssueCommentAndUser(IssueComment issueComment, UserEntity user);

}
