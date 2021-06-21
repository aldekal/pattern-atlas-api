package io.github.patternatlas.api.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.patternatlas.api.entities.PatternViewDirectedEdge;
import io.github.patternatlas.api.entities.PatternViewUndirectedEdge;
import io.github.patternatlas.api.entities.PatternViewUndirectedEdgeId;

@RepositoryRestResource(exported = false)
public interface PatternViewUndirectedEdgeRepository extends JpaRepository<PatternViewUndirectedEdge, PatternViewUndirectedEdgeId> {
    List<PatternViewUndirectedEdge> findByPatternViewId(UUID patternViewId);

    List<PatternViewDirectedEdge> findByUndirectedEdgeId(UUID undirectedEdgeId);

    boolean existsByPatternViewId(UUID patternViewId);

    boolean existsByUndirectedEdgeId(UUID undirectedEdgeId);
}
