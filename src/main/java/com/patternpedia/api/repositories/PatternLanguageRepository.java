package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.PatternLanguage;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface PatternLanguageRepository extends CrudRepository<PatternLanguage, UUID> {
    public Optional<PatternLanguage> findByUri(String uri);

    public boolean existsByUri(String uri);
}
