package io.github.patternatlas.api.exception;

import java.util.UUID;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatternLanguageNotFoundException extends ResourceNotFoundException {
    public PatternLanguageNotFoundException(String message) {
        super(message);
    }

    public PatternLanguageNotFoundException(UUID patternLanguageId) {
        super(String.format("PatternLanguage \"%s\" not found!", patternLanguageId));
    }
}
