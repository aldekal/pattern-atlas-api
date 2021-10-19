package io.github.patternatlas.api.repositories;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.patternatlas.api.entities.candidate.Candidate;

@RepositoryRestResource(exported = false)
public interface CandidateRepository extends JpaRepository<Candidate, UUID> {

    public Optional<Candidate> findByUri(String uri);
    public boolean existsByUri(String uri);
    public boolean existsByName(String name);

    @Query(value = "SELECT * FROM candidate c WHERE c.pattern_language_id = :languageId", nativeQuery = true)
    public List<Candidate> findAllByLanguageId(@Param("languageId") UUID languageId);
}
