package io.github.patternatlas.api.rest.model;

import java.net.URL;
import java.util.UUID;

import io.github.patternatlas.api.entities.PatternLanguage;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PatternLanguageModel {
    private UUID id;

    private String uri;

    private String name;

    private URL logo;

    private int patternCount;

    private String creativeCommonsReference;

    public static PatternLanguageModel toModel(PatternLanguage patternLanguage) {
        PatternLanguageModel model = new PatternLanguageModel();
        model.setId(patternLanguage.getId());
        model.setUri(patternLanguage.getUri());
        model.setName(patternLanguage.getName());
        model.setLogo(patternLanguage.getLogo());
        model.setPatternCount(patternLanguage.getPatterns().size());
        model.setCreativeCommonsReference(patternLanguage.getCreativeCommonsReference());
        return model;
    }
}
