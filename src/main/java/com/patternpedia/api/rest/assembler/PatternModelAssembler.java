package com.patternpedia.api.rest.assembler;

import com.patternpedia.api.rest.controller.PatternController;
import com.patternpedia.api.rest.representation.Pattern;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class PatternModelAssembler extends RepresentationModelAssemblerSupport<com.patternpedia.api.entities.Pattern, Pattern> {

    public PatternModelAssembler() {
        super(PatternController.class, Pattern.class);
    }

    @Override
    public Pattern toModel(com.patternpedia.api.entities.Pattern pattern) {
        Pattern patternModel = new Pattern();
        patternModel.setId(pattern.getId());
        patternModel.setUri(pattern.getUri());
        patternModel.setName(pattern.getName());
        return patternModel;
    }
}
