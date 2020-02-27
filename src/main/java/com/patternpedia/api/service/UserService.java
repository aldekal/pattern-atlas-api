package com.patternpedia.api.service;

import java.util.List;
import com.patternpedia.api.entities.PatternEvolution;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.util.UUID;

public interface PatternEvolutionService {

    PatternEvolution createPatternEvolution(PatternEvolution patternEvolution);

    PatternEvolution updatePatternEvolution(PatternEvolution patternEvolution);

    void deletePatternEvolution(UUID patternEvolutionId);

    PatternEvolution getPatternEvolutionById(UUID patternEvolutionId);

    PatternEvolution getPatternEvolutionByUri(String uri);

    List<PatternEvolution> getAllPatternEvolutions();
}
