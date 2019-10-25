package com.patternpedia.api.service;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.validator.PatternContentConstraint;

import javax.validation.Valid;
import java.util.UUID;

public interface PatternService {
    Pattern createPattern(@Valid @PatternContentConstraint Pattern pattern);

    Pattern updatePattern(@Valid @PatternContentConstraint Pattern pattern);

    void deletePattern(Pattern pattern);

    Pattern getPatternById(UUID patternId);

}
