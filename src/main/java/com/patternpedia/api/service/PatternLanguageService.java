package com.patternpedia.api.service;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.PatternSchema;

import java.util.List;
import java.util.UUID;

public interface PatternLanguageService {
    PatternLanguage createPatternLanguage(PatternLanguage patternLanguage);

    List<PatternLanguage> getAllPatternLanguages();

    PatternLanguage getPatternLanguageById(UUID patternLanguageId);

    PatternLanguage updatePatternLanguage(PatternLanguage patternLanguage);

    void deletePatternLanguage(PatternLanguage patternLanguage);

    List<Pattern> getAllPatternsOfPatternLanguage(UUID patternLanguageId);

    Pattern getPatternOfPatternLanguageById(UUID patternLanguageId, UUID patternId);

    void deletePatternOfPatternLanguage(UUID patternLanguageId, UUID patternId);

    PatternSchema createPatternSchemaAndAddToPatternLanguage(UUID patternLanguageId, PatternSchema patternSchema);

    PatternSchema getPatternSchemaByPatternLanguageId(UUID patternLanguageId);

    PatternSchema updatePatternSchemaByPatternLanguageId(UUID patternLanguageId, PatternSchema patternSchema);

}
