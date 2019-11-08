package com.patternpedia.api.service;

import com.patternpedia.api.entities.*;

import java.util.List;
import java.util.UUID;

public interface PatternLanguageService {

    PatternLanguage createPatternLanguage(PatternLanguage patternLanguage);

    List<PatternLanguage> getAllPatternLanguages();

    PatternLanguage getPatternLanguageById(UUID patternLanguageId);

    PatternLanguage getPatternLanguageByUri(String uri);

    PatternLanguage updatePatternLanguage(PatternLanguage patternLanguage);

    void deletePatternLanguage(UUID patternLanguageId);

    List<Pattern> getAllPatternsOfPatternLanguage(UUID patternLanguageId);

    Pattern getPatternOfPatternLanguageById(UUID patternLanguageId, UUID patternId);

    void deletePatternOfPatternLanguage(UUID patternLanguageId, UUID patternId);

    PatternSchema createPatternSchemaAndAddToPatternLanguage(UUID patternLanguageId, PatternSchema patternSchema);

    PatternSchema getPatternSchemaByPatternLanguageId(UUID patternLanguageId);

    PatternSchema updatePatternSchemaByPatternLanguageId(UUID patternLanguageId, PatternSchema patternSchema);

    List<DirectedEdge> getDirectedEdgesByPatternLanguageId(UUID patternLanguageId);

    List<UndirectedEdge> getUndirectedEdgesByPatternLanguageId(UUID patternLanguageId);

}
