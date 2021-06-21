package io.github.ust.quantil.patternatlas.api.repositories;

import java.util.Optional;
import java.util.UUID;

import io.github.ust.quantil.patternatlas.api.entities.Pattern;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface PatternRepository extends CrudRepository<Pattern, UUID> {

    Optional<Pattern> findByUri(String uri);

    boolean existsByUri(String uri);
}
