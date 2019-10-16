package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.PatternLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@RepositoryRestResource
public interface PatternLanguageRepository extends JpaRepository<PatternLanguage, UUID> {

    public Optional<PatternLanguage> findByUri(String uri);

    public boolean existsByUri(String uri);

}
