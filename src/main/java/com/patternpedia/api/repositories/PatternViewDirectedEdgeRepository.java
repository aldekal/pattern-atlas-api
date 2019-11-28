package com.patternpedia.api.repositories;

import java.util.List;
import java.util.UUID;

import com.patternpedia.api.entities.PatternViewDirectedEdge;
import com.patternpedia.api.entities.PatternViewDirectedEdgeId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PatternViewDirectedEdgeRepository extends JpaRepository<PatternViewDirectedEdge, PatternViewDirectedEdgeId> {
    List<PatternViewDirectedEdge> findByPatternViewId(UUID patternViewId);

    List<PatternViewDirectedEdge> findByDirectedEdgeId(UUID directedEdgeId);

    boolean existsByPatternViewId(UUID patternViewId);

    boolean existsByDirectedEdgeId(UUID directedEdgeId);
}
