package io.github.patternatlas.api.repositories;

import io.github.patternatlas.api.entities.issue.comment.IssueComment;
import io.github.patternatlas.api.entities.issue.evidence.IssueEvidence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IssueEvidenceRepository extends JpaRepository<IssueEvidence, UUID> {

}
