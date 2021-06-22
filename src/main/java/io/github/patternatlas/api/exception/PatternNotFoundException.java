package io.github.patternatlas.api.exception;

import java.util.UUID;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.github.patternatlas.api.entities.PatternGraphType;
import io.github.patternatlas.api.entities.PatternLanguage;
import io.github.patternatlas.api.entities.PatternView;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatternNotFoundException extends ResourceNotFoundException {
    public PatternNotFoundException(String message) {
        super(message);
    }

    public PatternNotFoundException(UUID patternId) {
        super(String.format("Pattern \"%s\" not found!", patternId));
    }

    public PatternNotFoundException(PatternView patternView, UUID patternId) {
        super(String.format("Pattern \"%s\" is not part of PatternView \"%s\"!", patternId, patternView.getId()));
    }

    public PatternNotFoundException(PatternLanguage patternLanguage, UUID patternId) {
        super(String.format("Pattern \"%s\" is not part of PatternLanguage \"%s\"!", patternId, patternLanguage.getId()));
    }

    public PatternNotFoundException(UUID graphId, UUID patternId, PatternGraphType patternGraphType) {
        super(createMessage(graphId, patternId, patternGraphType));
    }

    private static String createMessage(UUID graphId, UUID patternId, PatternGraphType patternGraphType) {
        switch (patternGraphType) {
            case PATTERN_VIEW:
                return String.format("Pattern \"%s\" is not part of PatternView \"%s\"!", patternId, graphId);
            case PATTERN_LANGUAGE:
                return String.format("Pattern \"%s\" is not part of PatternLanguage \"%s\"!", patternId, graphId);
            default:
                return String.format("Pattern \"%s\" not found!", patternId);
        }
    }
}
