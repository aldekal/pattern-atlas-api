package io.github.patternatlas.api.rest.exception;

import io.github.patternatlas.api.exception.DirectedEdgeNotFoundException;
import io.github.patternatlas.api.exception.NullPatternSchemaException;
import io.github.patternatlas.api.exception.PatternLanguageNotFoundException;
import io.github.patternatlas.api.exception.PatternNotFoundException;
import io.github.patternatlas.api.exception.PatternSchemaNotFoundException;
import io.github.patternatlas.api.exception.PatternViewNotFoundException;
import io.github.patternatlas.api.exception.UndirectedEdgeNotFoundException;
import io.github.patternatlas.api.rest.model.ErrorMessageDTO;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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
            ResourceNotFoundException.class,
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

    @ExceptionHandler(value = {
            Exception.class
    })
    protected ResponseEntity<Object> handleStorageExceptions(Exception ex, WebRequest request) {
        ex.printStackTrace();
        ErrorMessageDTO errorMessage = new ErrorMessageDTO(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
