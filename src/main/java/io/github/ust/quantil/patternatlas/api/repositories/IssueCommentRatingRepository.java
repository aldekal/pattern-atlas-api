package io.github.ust.quantil.patternatlas.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.ust.quantil.patternatlas.api.entities.issue.IssueComment;
import io.github.ust.quantil.patternatlas.api.entities.issue.rating.IssueCommentRating;
import io.github.ust.quantil.patternatlas.api.entities.issue.rating.IssueCommentRatingKey;
import io.github.ust.quantil.patternatlas.api.entities.user.UserEntity;

@RepositoryRestResource(exported = false)
public interface IssueCommentRatingRepository extends JpaRepository<IssueCommentRating, IssueCommentRatingKey> {

    IssueCommentRating findByIssueCommentAndUser(IssueComment issueComment, UserEntity user);
}
