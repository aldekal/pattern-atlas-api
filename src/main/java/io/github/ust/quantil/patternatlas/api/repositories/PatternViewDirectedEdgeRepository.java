package io.github.ust.quantil.patternatlas.api.repositories;

import java.util.List;
import java.util.UUID;

import io.github.ust.quantil.patternatlas.api.entities.PatternViewDirectedEdge;
import io.github.ust.quantil.patternatlas.api.entities.PatternViewDirectedEdgeId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface PatternViewDirectedEdgeRepository extends JpaRepository<PatternViewDirectedEdge, PatternViewDirectedEdgeId> {
    List<PatternViewDirectedEdge> findByPatternViewId(UUID patternViewId);

    List<PatternViewDirectedEdge> findByDirectedEdgeId(UUID directedEdgeId);

    boolean existsByPatternViewId(UUID patternViewId);

    boolean existsByDirectedEdgeId(UUID directedEdgeId);
}
