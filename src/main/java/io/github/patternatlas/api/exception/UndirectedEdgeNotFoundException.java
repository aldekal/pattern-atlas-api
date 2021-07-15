package com.patternpedia.api.exception;

import java.util.UUID;

import com.patternpedia.api.entities.PatternGraphType;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.PatternView;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

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
