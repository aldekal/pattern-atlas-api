package com.patternatlas.api.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NullPatternSchemaException extends RuntimeException {

    public NullPatternSchemaException(String message) {
        super(message);
    }
}
