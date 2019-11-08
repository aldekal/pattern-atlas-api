package com.patternpedia.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.UndirectedEdge;

import java.util.UUID;

public interface PatternRelationDescriptorService {

    DirectedEdge createDirectedEdge(DirectedEdge directedEdge, UUID patternLanguageId);

    DirectedEdge getDirectedEdgeById(UUID id);

    void deleteDirectedEdge(UUID directedEdgeId);

    UndirectedEdge createUndirectedEdge(UndirectedEdge undirectedEdge, UUID patternLanguageId);

    UndirectedEdge getUndirectedEdgeById(UUID id);

    void deleteUndirectedEdge(UUID undirectedEdgeId);
}
