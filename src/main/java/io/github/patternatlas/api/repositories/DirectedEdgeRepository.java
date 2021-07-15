package io.github.patternatlas.api.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.patternatlas.api.entities.DirectedEdge;
import io.github.patternatlas.api.entities.Pattern;

@RepositoryRestResource(exported = false)
public interface DirectedEdgeRepository extends JpaRepository<DirectedEdge, UUID> {
    Optional<List<DirectedEdge>> findBySource(Pattern pattern);

    Optional<List<DirectedEdge>> findByTarget(Pattern pattern);

    boolean existsBySourceIdAndTargetId(UUID sourcePatternId, UUID targetPatternId);
}
