package com.patternpedia.api.service;

import java.util.List;
import java.util.UUID;

import com.patternpedia.api.entities.edge.DirectedEdge;
import com.patternpedia.api.entities.pattern.pattern.Pattern;
import com.patternpedia.api.entities.edge.UndirectedEdge;

import org.springframework.transaction.annotation.Transactional;

public interface PatternRelationDescriptorService {

    @Transactional
    DirectedEdge createDirectedEdge(DirectedEdge directedEdge);

    @Transactional(readOnly = false)
    DirectedEdge getDirectedEdgeById(UUID id);

    @Transactional(readOnly = false)
    List<DirectedEdge> findDirectedEdgeBySource(Pattern pattern);

    @Transactional(readOnly = false)
    List<DirectedEdge> findDirectedEdgeByTarget(Pattern pattern);

    @Transactional
    DirectedEdge updateDirectedEdge(DirectedEdge directedEdge);

    @Transactional
    void deleteDirectedEdgeById(UUID directedEdgeId);

    @Transactional
    void deleteDirectedEdge(DirectedEdge directedEdge);

    @Transactional
    void deleteAllDirectedEdges(Iterable<DirectedEdge> directedEdges);

    @Transactional
    UndirectedEdge createUndirectedEdge(UndirectedEdge undirectedEdge);

    @Transactional(readOnly = true)
    UndirectedEdge getUndirectedEdgeById(UUID id);

    @Transactional(readOnly = true)
    List<UndirectedEdge> findUndirectedEdgeByPattern(Pattern pattern);

    @Transactional
    UndirectedEdge updateUndirectedEdge(UndirectedEdge undirectedEdge);

    @Transactional
    void deleteUndirectedEdgeById(UUID undirectedEdgeId);

    @Transactional
    void deleteUndirectedEdge(UndirectedEdge undirectedEdge);

    @Transactional
    void deleteAllUndirectedEdges(Iterable<UndirectedEdge> undirectedEdges);
}
