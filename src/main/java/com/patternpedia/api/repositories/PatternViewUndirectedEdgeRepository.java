package com.patternpedia.api.repositories;

import java.util.List;
import java.util.UUID;

import com.patternpedia.api.entities.PatternViewDirectedEdge;
import com.patternpedia.api.entities.PatternViewUndirectedEdge;
import com.patternpedia.api.entities.PatternViewUndirectedEdgeId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PatternViewUndirectedEdgeRepository extends JpaRepository<PatternViewUndirectedEdge, PatternViewUndirectedEdgeId> {
    List<PatternViewUndirectedEdge> findByPatternViewId(UUID patternViewId);

    List<PatternViewDirectedEdge> findByUndirectedEdgeId(UUID undirectedEdgeId);

    boolean existsByPatternViewId(UUID patternViewId);

    boolean existsByUndirectedEdgeId(UUID undirectedEdgeId);
}
