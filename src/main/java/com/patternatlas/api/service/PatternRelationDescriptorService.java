package com.patternatlas.api.service;

import java.util.List;
import java.util.UUID;

import com.patternatlas.api.entities.DirectedEdge;
import com.patternatlas.api.entities.Pattern;
import com.patternatlas.api.entities.UndirectedEdge;

import org.springframework.transaction.annotation.Transactional;

public interface PatternRelationDescriptorService {

    @Transactional
    DirectedEdge createDirectedEdge(DirectedEdge directedEdge);

    @Transactional(readOnly = false)
    DirectedEdge getDirectedEdgeById(UUID id);

    List<DirectedEdge> findDirectedEdgeBySource(Pattern pattern);

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
