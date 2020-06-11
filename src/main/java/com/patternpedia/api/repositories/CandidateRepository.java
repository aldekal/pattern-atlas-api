package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.candidate.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CandidateRepository extends JpaRepository<Candidate, UUID> {

    public Optional<Candidate> findByUri(String uri);
    public boolean existsByUri(String uri);
    public boolean existsByName(String name);


}
