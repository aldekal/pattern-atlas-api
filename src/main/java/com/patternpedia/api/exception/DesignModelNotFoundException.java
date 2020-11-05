package com.patternpedia.api.exception;

import com.patternpedia.api.entities.designmodel.DesignModel;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.UUID;

public class DesignModelNotFoundException extends ResourceNotFoundException {

    public DesignModelNotFoundException(String message) {
        super(message);
    }

    public DesignModelNotFoundException(UUID designModelId) {
        super(String.format("DesignModel \"%s\" not found!", designModelId));
    }

    public DesignModelNotFoundException(DesignModel designModel) {
        super(String.format("DesignModel \"%s\" not found!", designModel.getId()));
    }
}
