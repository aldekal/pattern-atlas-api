package io.github.patternatlas.api.exception;

import java.util.UUID;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import io.github.patternatlas.api.entities.designmodel.DesignModelPatternInstance;

public class DesignModelPatternInstanceNotFoundException extends ResourceNotFoundException {

    public DesignModelPatternInstanceNotFoundException(String message) {
        super(message);
    }

    public DesignModelPatternInstanceNotFoundException(UUID designModelId, UUID patternInstanceId) {
        super(String.format("PatternInstance \"%s\" not found in DesignModel \"%s\"!", patternInstanceId, designModelId));
    }

    public DesignModelPatternInstanceNotFoundException(DesignModelPatternInstance patternInstance) {
        super(String.format("PatternInstance \"%s\" not found in DesignModel \"%s\"!", patternInstance.getPatternInstanceId(), patternInstance.getDesignModel().getId()));
    }
}
