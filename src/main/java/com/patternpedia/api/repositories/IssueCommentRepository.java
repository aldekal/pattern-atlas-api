package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.issue.comment.IssueComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface IssueCommentRepository extends JpaRepository<IssueComment, UUID> {

}
