package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.issue.CommentIssue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentIssueRepository extends JpaRepository<CommentIssue, UUID> {

}
