package io.github.patternatlas.api.exception;

import java.util.UUID;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import io.github.patternatlas.api.entities.PatternGraphType;
import io.github.patternatlas.api.entities.PatternLanguage;
import io.github.patternatlas.api.entities.PatternView;

public class UndirectedEdgeNotFoundException extends ResourceNotFoundException {
    public UndirectedEdgeNotFoundException(String message) {
        super(message);
    }

    public UndirectedEdgeNotFoundException(PatternView patternView, UUID undirectedEdgeId) {
        super(String.format("UndirectedEdge \"%s\" is not part of PatternView \"%s\"", undirectedEdgeId, patternView.getId()));
    }

    public UndirectedEdgeNotFoundException(PatternLanguage patternLanguage, UUID undirectedEdgeId) {
        super(String.format("UndirectedEdge \"%s\" is not part of PatternLanguage \"%s\"", undirectedEdgeId, patternLanguage.getId()));
    }

    public UndirectedEdgeNotFoundException(UUID graphId, UUID undirectedEdgeId, PatternGraphType patternGraphType) {
        super(createMessage(graphId, undirectedEdgeId, patternGraphType));
    }

    private static String createMessage(UUID graphId, UUID undirectedEdgeId, PatternGraphType patternGraphType) {
        switch (patternGraphType) {
            case PATTERN_VIEW:
                return String.format("UndirectedEdge \"%s\" is not part of PatternView \"%s\"", undirectedEdgeId, graphId);
            case PATTERN_LANGUAGE:
                return String.format("UndirectedEdge \"%s\" is not part of PatternLanguage \"%s\"", undirectedEdgeId, graphId);
            default:
                return String.format("UndirectedEdge \"%s\" not found", undirectedEdgeId);
        }
    }
}
