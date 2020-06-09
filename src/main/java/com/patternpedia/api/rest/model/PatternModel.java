package com.patternpedia.api.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class PatternModel {

    protected UUID id;

    protected String uri;

    protected String name;

    protected String iconUrl;

    protected UUID patternLanguageId;

    protected String patternLanguageName;

    @JsonIgnore
    protected Pattern pattern;

    private PatternModel(Pattern pattern) {
        this.pattern = pattern;
        this.id = pattern.getId();
        this.uri = pattern.getUri();
        this.name = pattern.getName();
        this.iconUrl = pattern.getIconUrl();
        PatternLanguage patternLanguage = pattern.getPatternLanguage();
        this.patternLanguageId = patternLanguage.getId();
        this.patternLanguageName = patternLanguage.getName();
    }

    public static PatternModel from(Pattern pattern) {
        return new PatternModel(pattern);
    }
}
