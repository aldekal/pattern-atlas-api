package io.github.ust.quantil.patternatlas.api.repositories;

import io.github.ust.quantil.patternatlas.api.entities.PatternView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface PatternViewRepository extends JpaRepository<PatternView, UUID> {

    Optional<PatternView> findByUri(String uri);

    boolean existsByUri(String uri);
}
