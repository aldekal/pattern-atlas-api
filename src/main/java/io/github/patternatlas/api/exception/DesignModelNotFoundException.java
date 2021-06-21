package io.github.patternatlas.api.exception;

import java.util.UUID;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import io.github.patternatlas.api.entities.designmodel.DesignModel;

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
