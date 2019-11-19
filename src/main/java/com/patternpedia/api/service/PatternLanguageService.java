package com.patternpedia.api.service;

import com.patternpedia.api.entities.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface PatternLanguageService {

    @Transactional
    PatternLanguage createPatternLanguage(PatternLanguage patternLanguage);

    @Transactional(readOnly = true)
    List<PatternLanguage> getPatternLanguages();

    @Transactional(readOnly = true)
    PatternLanguage getPatternLanguageById(UUID patternLanguageId);

    @Transactional(readOnly = true)
    PatternLanguage getPatternLanguageByUri(String uri);

    @Transactional
    PatternLanguage updatePatternLanguage(PatternLanguage patternLanguage);

    @Transactional
    void deletePatternLanguage(UUID patternLanguageId);



    @Transactional
    Pattern createPatternAndAddToPatternLanguage(UUID patternLanguageId, Pattern pattern);

    @Transactional(readOnly = true)
    List<Pattern> getPatternsOfPatternLanguage(UUID patternLanguageId);

    @Transactional(readOnly = true)
    Pattern getPatternOfPatternLanguageById(UUID patternLanguageId, UUID patternId);

    @Transactional
    void deletePatternOfPatternLanguage(UUID patternLanguageId, UUID patternId);



    @Transactional
    PatternSchema createPatternSchemaAndAddToPatternLanguage(UUID patternLanguageId, PatternSchema patternSchema);

    @Transactional(readOnly = true)
    PatternSchema getPatternSchemaByPatternLanguageId(UUID patternLanguageId);

    @Transactional
    PatternSchema updatePatternSchemaOfPatternLanguage(UUID patternLanguageId, PatternSchema patternSchema);



    @Transactional
    DirectedEdge createDirectedEdgeAndAddToPatternLanguage(UUID patternLanguageId, DirectedEdge directedEdge);

    @Transactional(readOnly = true)
    List<DirectedEdge> getDirectedEdgesOfPatternLanguage(UUID patternLanguageId);

    @Transactional
    UndirectedEdge createUndirectedEdgeAndAddToPatternLanguage(UUID patternLanguageId, UndirectedEdge undirectedEdge);

    @Transactional(readOnly = true)
    List<UndirectedEdge> getUndirectedEdgesOfPatternLanguage(UUID patternLanguageId);

}
