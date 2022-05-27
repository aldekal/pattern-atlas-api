package io.github.patternatlas.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.patternatlas.api.entities.issue.comment.IssueComment;
import io.github.patternatlas.api.entities.issue.comment.IssueCommentRating;
import io.github.patternatlas.api.entities.shared.CompositeKey;
import io.github.patternatlas.api.entities.user.UserEntity;

@RepositoryRestResource(exported = false)
public interface IssueCommentRatingRepository extends JpaRepository<IssueCommentRating, CompositeKey> {

    IssueCommentRating findByIssueCommentAndUser(IssueComment issueComment, UserEntity user);

    boolean existsByIdAndRating(CompositeKey compositeKey, int rating);

}
