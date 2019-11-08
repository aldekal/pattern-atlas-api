package com.patternpedia.api.rest.assembler;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.rest.controller.PatternController;
import com.patternpedia.api.rest.representationModel.PatternModel;
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
