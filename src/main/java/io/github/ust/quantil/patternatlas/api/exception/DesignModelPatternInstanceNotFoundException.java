package io.github.ust.quantil.patternatlas.api.exception;

import io.github.ust.quantil.patternatlas.api.entities.designmodel.DesignModelPatternInstance;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.UUID;

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
