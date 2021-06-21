package io.github.ust.quantil.patternatlas.api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.ust.quantil.patternatlas.api.entities.candidate.Candidate;

@RepositoryRestResource(exported = false)
public interface CandidateRepository extends JpaRepository<Candidate, UUID> {

    Optional<Candidate> findByUri(String uri);

    boolean existsByUri(String uri);
}
