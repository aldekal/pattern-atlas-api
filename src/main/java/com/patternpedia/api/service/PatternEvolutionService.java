package com.patternpedia.api.service;

import java.util.List;

import com.patternpedia.api.entities.evolution.CommentPatternEvolution;
import com.patternpedia.api.entities.evolution.PatternEvolution;

import java.util.UUID;

public interface PatternEvolutionService {

    /** CRUD  */
    PatternEvolution createPatternEvolution(PatternEvolution patternEvolution);

    PatternEvolution updatePatternEvolution(PatternEvolution patternEvolution);

    void deletePatternEvolution(UUID patternEvolutionId);

    PatternEvolution getPatternEvolutionById(UUID patternEvolutionId);

    PatternEvolution getPatternEvolutionByUri(String uri);

    List<PatternEvolution> getAllPatternEvolutions();

    /** Voting */
    PatternEvolution userRating(UUID patternEvolutionId, UUID userId, String rating);

    /** Comment */
    PatternEvolution createComment(UUID patternEvolutionId, UUID userId, CommentPatternEvolution commentPatternEvolution);
}
