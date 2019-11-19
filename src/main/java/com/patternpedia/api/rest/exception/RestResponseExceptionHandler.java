package com.patternpedia.api.rest.exception;

import com.patternpedia.api.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            PatternLanguageNotFoundException.class,
            PatternNotFoundException.class,
            PatternSchemaNotFoundException.class,
            PatternViewNotFoundException.class,
            DirectedEdgeNotFoundException.class,
            UndirectedEdgeNotFoundException.class
    })
    protected ResponseEntity<Object> handleEntityNotFoundExceptions(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {
            NullPatternSchemaException.class
    })
    protected ResponseEntity<Object> handleNullPatternSchemaException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}