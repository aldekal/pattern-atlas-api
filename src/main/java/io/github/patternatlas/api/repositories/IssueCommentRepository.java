package io.github.patternatlas.api.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.patternatlas.api.entities.issue.comment.IssueComment;

@RepositoryRestResource(exported = false)
public interface IssueCommentRepository extends JpaRepository<IssueComment, UUID> {

}
