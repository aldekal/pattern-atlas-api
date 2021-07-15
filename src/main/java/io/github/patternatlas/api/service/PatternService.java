package io.github.patternatlas.api.service;

import java.util.UUID;
import javax.validation.Valid;

import io.github.patternatlas.api.entities.Pattern;
import io.github.patternatlas.api.validator.PatternContentConstraint;

public interface PatternService {

    Pattern createPattern(@Valid @PatternContentConstraint Pattern pattern);

    Pattern updatePattern(@Valid @PatternContentConstraint Pattern pattern);

    void deletePattern(Pattern pattern);

    Pattern getPatternById(UUID patternId);

    Pattern getPatternByUri(String uri);
}
