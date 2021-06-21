package io.github.ust.quantil.patternatlas.api.exception;

import java.util.UUID;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CandidateNotFoundException extends ResourceNotFoundException {

    public CandidateNotFoundException(String message) {
        super(message);
    }

    public CandidateNotFoundException(UUID candidateId) {
        super(String.format("Candidate \"%s\" not found!", candidateId));
    }
}
