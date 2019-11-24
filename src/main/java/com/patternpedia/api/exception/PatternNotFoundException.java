package com.patternpedia.api.exception;

import java.util.UUID;

import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.PatternView;

import lombok.NoArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatternNotFoundException extends ResourceNotFoundException {
    public PatternNotFoundException(String message) {
        super(message);
    }

    public PatternNotFoundException(PatternView patternView, UUID patternId) {
        super(String.format("Pattern \"%s\" is not part of PatternView \"%s\"", patternId, patternView.getId()));
    }

    public PatternNotFoundException(PatternLanguage patternLanguage, UUID patternId) {
        super(String.format("Pattern \"%s\" is not part of PatternLanguage \"%s\"", patternId, patternLanguage.getId()));
    }
}
