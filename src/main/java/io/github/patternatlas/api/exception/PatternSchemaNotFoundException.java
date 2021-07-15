package com.patternpedia.api.exception;

import lombok.NoArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatternSchemaNotFoundException extends ResourceNotFoundException {
    public PatternSchemaNotFoundException(String message) {
        super(message);
    }
}
