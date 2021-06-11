package com.patternatlas.api.exception;

import lombok.NoArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@NoArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends ResourceNotFoundException {

    public CommentNotFoundException(String message) {
        super(message);
    }

    public CommentNotFoundException(UUID commentId) {
        super(String.format("Comment \"%s\" not found!", commentId));
    }
}
