package com.patternpedia.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.UndirectedEdge;

import java.util.UUID;

public interface PatternRelationDescriptorService {

    DirectedEdge createDirectedEdge(Pattern source, Pattern target, UUID patternLanguageId, JsonNode description);

    DirectedEdge getDirectedEdgeById(UUID id);

    void deleteDirectedEdge(UUID directedEdgeId);

    UndirectedEdge createUndirectedEdge(Pattern p1, Pattern p2, UUID patternLanguageId, JsonNode description);

    UndirectedEdge getUndirectedEdgeById(UUID id);

    void deleteUndirectedEdge(UUID undirectedEdgeId);
}
