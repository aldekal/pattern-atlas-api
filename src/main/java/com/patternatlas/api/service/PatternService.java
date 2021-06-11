package com.patternatlas.api.service;

import java.util.UUID;

import com.patternatlas.api.entities.Pattern;
import com.patternatlas.api.validator.PatternContentConstraint;

import javax.validation.Valid;

public interface PatternService {

    Pattern createPattern(@Valid @PatternContentConstraint Pattern pattern);

    Pattern updatePattern(@Valid @PatternContentConstraint Pattern pattern);

    void deletePattern(Pattern pattern);

    Pattern getPatternById(UUID patternId);

    Pattern getPatternByUri(String uri);
}
