package com.patternpedia.api.rest.assembler;

import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.rest.controller.PatternLanguageController;
import com.patternpedia.api.rest.representationModel.PatternLanguageModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class PatternLanguageModelAssembler extends RepresentationModelAssemblerSupport<PatternLanguage, PatternLanguageModel> {

    public PatternLanguageModelAssembler() {
        super(PatternLanguageController.class, PatternLanguageModel.class);
    }

    @Override
    public PatternLanguageModel toModel(PatternLanguage patternLanguage) {
        PatternLanguageModel patternLanguageModel = new PatternLanguageModel();
        patternLanguageModel.setId(patternLanguage.getId());
        patternLanguageModel.setUri(patternLanguage.getUri());
        patternLanguageModel.setName(patternLanguage.getName());
        patternLanguageModel.setLogo(patternLanguage.getLogo());
        PatternModelAssembler patternModelAssembler = new PatternModelAssembler();
        patternLanguageModel.setPatterns(patternModelAssembler.toCollectionModel(patternLanguage.getPatterns()));
        return patternLanguageModel;
    }
}
