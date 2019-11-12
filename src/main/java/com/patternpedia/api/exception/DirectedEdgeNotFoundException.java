package com.patternpedia.api.exception;

import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.PatternView;

import java.util.UUID;

public class DirectedEdgeNotFoundException extends RuntimeException {
    public DirectedEdgeNotFoundException(String message) {
        super(message);
    }

    public DirectedEdgeNotFoundException(PatternView patternView, UUID directedEdgeId) {
        super(String.format("DirectedEdge \"%s\" is not part of PatternView \"%s\"", directedEdgeId, patternView));
    }

    public DirectedEdgeNotFoundException(PatternLanguage patternLanguage, UUID directedEdgeId) {
        super(String.format("DirectedEdge \"%s\" is not part of PatternLanguage \"%s\"", directedEdgeId, patternLanguage.getId()));
    }
}
