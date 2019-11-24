package com.patternpedia.api.exception;

import java.util.UUID;

import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.PatternView;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

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
}
