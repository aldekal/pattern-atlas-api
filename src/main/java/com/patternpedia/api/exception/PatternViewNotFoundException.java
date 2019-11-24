package com.patternpedia.api.exception;

import java.util.UUID;

import com.patternpedia.api.entities.PatternView;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class PatternViewNotFoundException extends ResourceNotFoundException {

    public PatternViewNotFoundException(String message) {
        super(message);
    }

    public PatternViewNotFoundException(UUID patternViewId) {
        super(String.format("PatternView \"%s\" not found!", patternViewId));
    }

    public PatternViewNotFoundException(PatternView patternView) {
        super(String.format("PatternView \"%s\" not found!", patternView.getId()));
    }
}
