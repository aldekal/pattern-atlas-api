package com.patternpedia.api.rest.assembler;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.rest.representation.PatternModel;
import com.patternpedia.api.rest.controller.PatternController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class PatternModelAssembler extends RepresentationModelAssemblerSupport<Pattern, PatternModel> {

    public PatternModelAssembler() {
        super(PatternController.class, PatternModel.class);
    }

    @Override
    public PatternModel toModel(Pattern pattern) {
        PatternModel patternModel = new PatternModel();
        patternModel.setId(pattern.getId());
        patternModel.setUri(pattern.getUri());
        patternModel.setName(pattern.getName());
        return patternModel;
    }
}
