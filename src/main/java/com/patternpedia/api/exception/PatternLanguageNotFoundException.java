package com.patternpedia.api.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatternLanguageNotFoundException extends RuntimeException {
    public PatternLanguageNotFoundException(String message) {
        super(message);
    }
}
