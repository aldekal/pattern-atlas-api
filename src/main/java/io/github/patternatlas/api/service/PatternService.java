package io.github.patternatlas.api.service;

import java.util.UUID;
import javax.validation.Valid;

import io.github.patternatlas.api.entities.Pattern;
import io.github.patternatlas.api.validator.PatternContentConstraint;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

public interface PatternService {

    @PreAuthorize(value = "hasGlobalPermission(@PC.APPROVED_PATTERN_CREATE)")
    Pattern createPattern(@Valid @PatternContentConstraint Pattern pattern);

    @PreAuthorize(value = "hasResourcePermission(#pattern.id, @PC.APPROVED_PATTERN_EDIT)")
    Pattern updatePattern(@Valid @PatternContentConstraint Pattern pattern);

    @PreAuthorize(value = "hasResourcePermission(#pattern.id, @PC.APPROVED_PATTERN_DELETE)")
    void deletePattern(Pattern pattern);

    @PostAuthorize(value = "hasResourcePermission(returnObject.id, @PC.APPROVED_PATTERN_READ)")
    Pattern getPatternById(UUID patternId);

    @PostAuthorize(value = "hasResourcePermission(returnObject.id, @PC.APPROVED_PATTERN_READ)")
    Pattern getPatternByUri(String uri);
}
