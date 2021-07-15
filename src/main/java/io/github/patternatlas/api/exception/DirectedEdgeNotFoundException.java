package io.github.patternatlas.api.exception;

import java.util.UUID;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import io.github.patternatlas.api.entities.PatternGraphType;
import io.github.patternatlas.api.entities.PatternLanguage;
import io.github.patternatlas.api.entities.PatternView;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DirectedEdgeNotFoundException extends ResourceNotFoundException {
    public DirectedEdgeNotFoundException(String message) {
        super(message);
    }

    public DirectedEdgeNotFoundException(PatternView patternView, UUID directedEdgeId) {
        super(String.format("DirectedEdge \"%s\" is not part of PatternView \"%s\"", directedEdgeId, patternView));
    }

    public DirectedEdgeNotFoundException(PatternLanguage patternLanguage, UUID directedEdgeId) {
        super(String.format("DirectedEdge \"%s\" is not part of PatternLanguage \"%s\"", directedEdgeId, patternLanguage.getId()));
    }

    public DirectedEdgeNotFoundException(UUID graphId, UUID directedEdgeId, PatternGraphType patternGraphType) {
        super(createMessage(graphId, directedEdgeId, patternGraphType));
    }

    private static String createMessage(UUID graphId, UUID directedEdgeId, PatternGraphType patternGraphType) {
        switch (patternGraphType) {
            case PATTERN_VIEW:
                return String.format("DirectedEdge \"%s\" is not part of PatternView \"%s\"", directedEdgeId, graphId);
            case PATTERN_LANGUAGE:
                return String.format("DirectedEdge \"%s\" is not part of PatternLanguage \"%s\"", directedEdgeId, graphId);
            default:
                return String.format("DirectedEdge \"%s\" not found", directedEdgeId);
        }
    }
}
