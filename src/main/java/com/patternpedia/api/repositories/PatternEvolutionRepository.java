package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.pattern.evolution.PatternEvolution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

//@RepositoryRestResource(exported = false)
public interface PatternEvolutionRepository extends JpaRepository<PatternEvolution, UUID> {

    public Optional<PatternEvolution> findByUri(String uri);

    public boolean existsByUri(String uri);


}
