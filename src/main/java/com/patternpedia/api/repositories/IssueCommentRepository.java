package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.issue.comment.IssueComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IssueCommentRepository extends JpaRepository<IssueComment, UUID> {

}
