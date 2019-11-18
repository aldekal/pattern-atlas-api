package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.Pattern;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface PatternRepository extends CrudRepository<Pattern, UUID> {

    Optional<Pattern> findByUri(String uri);

    boolean existsByUri(String uri);

}
