package io.github.patternatlas.api.rest.model.shared;

import io.github.patternatlas.api.entities.PatternLanguage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class PatternLanguageSchemaModel {

    private UUID patternLanguageId;
    private String patternLanguageName;
    Collection<PatternSchemaModel> patternSchema = new ArrayList<>();

    public PatternLanguageSchemaModel(PatternLanguage patternLanguage) {
        this.patternLanguageId = patternLanguage.getId();
        this.patternLanguageName = patternLanguage.getName();
        this.patternSchema = patternLanguage.getPatternSchema().getPatternSectionSchemas().stream().map(patternSectionSchema -> new PatternSchemaModel(patternSectionSchema))
                .collect(Collectors.toList())
                .stream().sorted(Comparator.comparing(PatternSchemaModel::getPosition)).
                        collect(Collectors.toList());
    }

}
