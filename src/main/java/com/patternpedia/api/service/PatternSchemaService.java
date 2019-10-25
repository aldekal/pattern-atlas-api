package com.patternpedia.api.service;

import com.patternpedia.api.entities.PatternSchema;

import java.util.UUID;

public interface PatternSchemaService {

    PatternSchema createPatternSchema(PatternSchema patternSchema);

    PatternSchema updatePatternSchema(PatternSchema patternSchema);

    PatternSchema getPatternSchemaById(UUID id);

    void deletePatternSchemaById(UUID id);
}
