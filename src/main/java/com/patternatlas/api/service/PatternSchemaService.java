package com.patternatlas.api.service;

import java.util.UUID;

import com.patternatlas.api.entities.PatternSchema;

public interface PatternSchemaService {

    PatternSchema createPatternSchema(PatternSchema patternSchema);

    PatternSchema updatePatternSchema(PatternSchema patternSchema);

    PatternSchema getPatternSchemaById(UUID id);

    void deletePatternSchemaById(UUID id);
}
