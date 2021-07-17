package io.github.patternatlas.api.repositories;

import io.github.patternatlas.api.entities.candidate.evidence.CandidateEvidence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CandidateEvidenceRepository extends JpaRepository<CandidateEvidence, UUID> {
}
