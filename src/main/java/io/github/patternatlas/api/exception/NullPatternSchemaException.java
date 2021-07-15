package io.github.patternatlas.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NullPatternSchemaException extends RuntimeException {

    public NullPatternSchemaException(String message) {
        super(message);
    }
}
