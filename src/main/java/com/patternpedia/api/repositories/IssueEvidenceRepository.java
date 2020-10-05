package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.issue.comment.IssueComment;
import com.patternpedia.api.entities.issue.evidence.IssueEvidence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IssueEvidenceRepository extends JpaRepository<IssueEvidence, UUID> {

}
