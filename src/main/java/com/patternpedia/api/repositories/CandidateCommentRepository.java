package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.candidate.CandidateComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CandidateCommentRepository extends JpaRepository<CandidateComment, UUID> {

}
