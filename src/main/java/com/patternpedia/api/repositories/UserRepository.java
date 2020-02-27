package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.PatternLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PatternLanguageRepository extends JpaRepository<PatternLanguage, UUID> {

    public Optional<PatternLanguage> findByUri(String uri);

    public boolean existsByUri(String uri);

}
