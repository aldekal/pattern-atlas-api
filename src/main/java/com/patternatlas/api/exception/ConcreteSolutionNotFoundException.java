package com.patternatlas.api.exception;

import com.patternatlas.api.entities.designmodel.ConcreteSolution;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.UUID;

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
