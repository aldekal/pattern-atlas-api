package com.patternpedia.api.service;

import java.util.UUID;

import com.patternpedia.api.entities.pattern.pattern.Pattern;
import com.patternpedia.api.validator.PatternContentConstraint;

import javax.validation.Valid;

public interface PatternService {

    Pattern createPattern(@Valid @PatternContentConstraint Pattern pattern);

    Pattern updatePattern(@Valid @PatternContentConstraint Pattern pattern);

    void deletePattern(Pattern pattern);

    Pattern getPatternById(UUID patternId);

    Pattern getPatternByUri(String uri);
}
