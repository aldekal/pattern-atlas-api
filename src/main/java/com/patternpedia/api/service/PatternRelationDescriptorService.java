package com.patternpedia.api.service;

import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.UndirectedEdge;

import java.util.List;
import java.util.UUID;

public interface PatternRelationDescriptorService {

    DirectedEdge createDirectedEdge(DirectedEdge directedEdge, UUID patternLanguageId);

    DirectedEdge getDirectedEdgeById(UUID id);

    List<DirectedEdge> findDirectedEdgeBySource(Pattern pattern);

    List<DirectedEdge> findDirectedEdgeByTarget(Pattern pattern);

    void deleteDirectedEdge(UUID directedEdgeId);

    UndirectedEdge createUndirectedEdge(UndirectedEdge undirectedEdge, UUID patternLanguageId);

    UndirectedEdge getUndirectedEdgeById(UUID id);

    void deleteUndirectedEdge(UUID undirectedEdgeId);
}
