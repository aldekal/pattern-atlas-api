package io.github.patternatlas.api.service;

import java.util.UUID;

import io.github.patternatlas.api.entities.PatternSchema;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

public interface PatternSchemaService {

    @PreAuthorize(value = "hasResourcePermission(#patternSchema.patternLanguage.id, @PC.PATTERN_LANGUAGE_EDIT)")
    PatternSchema createPatternSchema(PatternSchema patternSchema);

    @PreAuthorize(value = "hasResourcePermission(#patternSchema.patternLanguage.id, @PC.PATTERN_LANGUAGE_EDIT)")
    PatternSchema updatePatternSchema(PatternSchema patternSchema);

    @PostAuthorize(value = "hasResourcePermission(#returnObject.patternLanguage.id, @PC.PATTERN_LANGUAGE_READ)")
    PatternSchema getPatternSchemaById(UUID id);

    @PreAuthorize(value = "hasResourcePermission(#schema.patternLanguage.id, @PC.PATTERN_LANGUAGE_EDIT)")
    void deletePatternSchema(PatternSchema schema);
}
