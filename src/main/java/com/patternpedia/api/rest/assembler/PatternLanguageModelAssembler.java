package com.patternpedia.api.rest.assembler;

import com.patternpedia.api.rest.controller.PatternLanguageController;
import com.patternpedia.api.rest.representation.Pattern;
import com.patternpedia.api.rest.representation.PatternLanguage;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import java.util.List;
import java.util.stream.Collectors;

public class PatternLanguageModelAssembler extends RepresentationModelAssemblerSupport<com.patternpedia.api.entities.PatternLanguage, PatternLanguage> {

    public PatternLanguageModelAssembler() {
        super(PatternLanguageController.class, PatternLanguage.class);
    }

    @Override
    public PatternLanguage toModel(com.patternpedia.api.entities.PatternLanguage patternLanguage) {
        PatternLanguage patternLanguageModel = new PatternLanguage();
        patternLanguageModel.setId(patternLanguage.getId());
        patternLanguageModel.setUri(patternLanguage.getUri());
        patternLanguageModel.setName(patternLanguage.getName());
        patternLanguageModel.setLogo(patternLanguage.getLogo());
        PatternModelAssembler patternModelAssembler = new PatternModelAssembler();
        List<Pattern> collect = patternLanguage.getPatterns().stream()
                .map(patternModelAssembler::toModel)
                .collect(Collectors.toList());
        patternLanguageModel.setPatterns(collect);
        return patternLanguageModel;
    }
}
