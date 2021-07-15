package io.github.patternatlas.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NullUserException extends RuntimeException {

    public NullUserException(String message) {
        super(message);
    }
}
