package com.patternpedia.api.exception;

import com.patternpedia.api.entities.PatternView;

import java.util.UUID;

public class PatternViewNotFoundException extends RuntimeException {

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
