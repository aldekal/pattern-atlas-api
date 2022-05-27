package io.github.patternatlas.api.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.patternatlas.api.entities.candidate.comment.CandidateComment;

@RepositoryRestResource(exported = false)
public interface CandidateCommentRepository extends JpaRepository<CandidateComment, UUID> {

}
