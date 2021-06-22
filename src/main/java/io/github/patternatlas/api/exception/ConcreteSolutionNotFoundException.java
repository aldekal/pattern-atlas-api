package io.github.patternatlas.api.exception;

import java.util.UUID;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import io.github.patternatlas.api.entities.designmodel.ConcreteSolution;

public class ConcreteSolutionNotFoundException extends ResourceNotFoundException {

    public ConcreteSolutionNotFoundException(String message) {
        super(message);
    }

    public ConcreteSolutionNotFoundException(UUID concreteSolutionId) {
        super(String.format("ConcreteSolution \"%s\" not found!", concreteSolutionId));
    }

    public ConcreteSolutionNotFoundException(ConcreteSolution concreteSolution) {
        super(String.format("ConcreteSolution \"%s\" not found!", concreteSolution.getId()));
    }
}
