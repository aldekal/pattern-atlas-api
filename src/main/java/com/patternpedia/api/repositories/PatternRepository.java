package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.Pattern;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface PatternRepository extends CrudRepository<Pattern, UUID> {
    public Optional<Pattern> findByUri(String uri);

    public boolean existsByUri(String uri);
}
