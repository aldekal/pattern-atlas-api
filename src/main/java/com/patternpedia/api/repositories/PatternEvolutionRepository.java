package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternEvolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.UUID;

//@RepositoryRestResource(exported = false)
public interface PatternEvolutionRepository extends JpaRepository<PatternEvolution, UUID> {

    public Optional<PatternEvolution> findByUri(String uri);

    public boolean existsByUri(String uri);
}
