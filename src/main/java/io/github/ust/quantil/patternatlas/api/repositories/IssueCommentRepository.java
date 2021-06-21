package io.github.ust.quantil.patternatlas.api.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.ust.quantil.patternatlas.api.entities.issue.IssueComment;

@RepositoryRestResource(exported = false)
public interface IssueCommentRepository extends JpaRepository<IssueComment, UUID> {

}
