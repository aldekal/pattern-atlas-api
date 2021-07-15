package io.github.patternatlas.api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.patternatlas.api.entities.Pattern;

@RepositoryRestResource(exported = false)
public interface PatternRepository extends CrudRepository<Pattern, UUID> {

    Optional<Pattern> findByUri(String uri);

    boolean existsByUri(String uri);
}
