package io.github.patternatlas.api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.patternatlas.api.entities.PatternView;

@RepositoryRestResource(exported = false)
public interface PatternViewRepository extends JpaRepository<PatternView, UUID> {

    Optional<PatternView> findByUri(String uri);

    boolean existsByUri(String uri);
}
