package com.patternpedia.api.service;

import java.util.List;
import com.patternpedia.api.entities.PatternEvolution;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface PatternEvolutionService {

    PatternEvolution createPatternEvolution(PatternEvolution patternEvolution);

    PatternEvolution updatePatternEvolution(PatternEvolution patternEvolution);

    void deletePatternEvolution(PatternEvolution patternEvolution);

    PatternEvolution getPatternEvolutionById(UUID patternEvolutionId);

//    PatternEvolution getPatternEvolutionByUri(String uri);

    List<PatternEvolution> getPatternEvolutions();
}
