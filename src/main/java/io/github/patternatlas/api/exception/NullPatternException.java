package io.github.patternatlas.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NullPatternException extends RuntimeException {
    public NullPatternException(String message) {
        super(message);
    }
}
