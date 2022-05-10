package io.github.patternatlas.api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import io.github.patternatlas.api.entities.DirectedEdge;
import io.github.patternatlas.api.entities.Pattern;
import io.github.patternatlas.api.entities.PatternLanguage;
import io.github.patternatlas.api.entities.PatternSchema;
import io.github.patternatlas.api.exception.UndirectedEdgeNotFoundException;
import io.github.patternatlas.api.rest.model.CreateDirectedEdgeRequest;
import io.github.patternatlas.api.rest.model.CreateUndirectedEdgeRequest;
import io.github.patternatlas.api.entities.UndirectedEdge;

public interface PatternLanguageService {

    @PreAuthorize(value = "hasGlobalPermission(@PC.PATTERN_LANGUAGE_CREATE)")
    PatternLanguage createPatternLanguage(PatternLanguage patternLanguage);

    @PostFilter("hasResourcePermission(filterObject.id, @PC.PATTERN_LANGUAGE_READ)")
    List<PatternLanguage> getPatternLanguages();

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_READ)")
    PatternLanguage getPatternLanguageById(UUID patternLanguageId);

    @PostAuthorize(value = "hasResourcePermission(returnObject.id, @PC.PATTERN_LANGUAGE_READ)")
    PatternLanguage getPatternLanguageByUri(String uri);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguage.id, @PC.PATTERN_LANGUAGE_EDIT)")
    PatternLanguage updatePatternLanguage(PatternLanguage patternLanguage);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_DELETE)")
    void deletePatternLanguage(UUID patternLanguageId);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_EDIT) && hasGlobalPermission(@PC.APPROVED_PATTERN_CREATE)")
    Pattern createPatternAndAddToPatternLanguage(UUID patternLanguageId, Pattern pattern);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_READ)")
    @PostFilter("hasResourcePermission(filterObject.id, @PC.APPROVED_PATTERN_READ)")
    List<Pattern> getPatternsOfPatternLanguage(UUID patternLanguageId);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_READ) && hasResourcePermission(#patternId, @PC.APPROVED_PATTERN_READ)")
    Pattern getPatternOfPatternLanguageById(UUID patternLanguageId, UUID patternId);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_EDIT) && hasResourcePermission(#patternId, @PC.APPROVED_PATTERN_DELETE)")
    void deletePatternOfPatternLanguage(UUID patternLanguageId, UUID patternId);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_EDIT)")
    PatternSchema createPatternSchemaAndAddToPatternLanguage(UUID patternLanguageId, PatternSchema patternSchema);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_READ)")
    PatternSchema getPatternSchemaByPatternLanguageId(UUID patternLanguageId);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_EDIT)")
    PatternSchema updatePatternSchemaOfPatternLanguage(UUID patternLanguageId, PatternSchema patternSchema);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_READ)")
    Object getGraphOfPatternLanguage(UUID patternLanguageId);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_EDIT)")
    Object createGraphOfPatternLanguage(UUID patternLanguageId, Object graph);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_EDIT)")
    Object updateGraphOfPatternLanguage(UUID patternLanguageId, Object graph);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_EDIT)")
    void deleteGraphOfPatternLanguage(UUID patternLanguageId);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_EDIT)")
    DirectedEdge createDirectedEdgeAndAddToPatternLanguage(UUID patternLanguageId, CreateDirectedEdgeRequest createDirectedEdgeRequest);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_READ)")
    List<DirectedEdge> getDirectedEdgesOfPatternLanguage(UUID patternLanguageId);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_READ)")
    DirectedEdge getDirectedEdgeOfPatternLanguageById(UUID patternLanguageId, UUID directedEdgeId);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_EDIT)")
    DirectedEdge updateDirectedEdgeOfPatternLanguage(UUID patternLanguageId, DirectedEdge directedEdge);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_EDIT)")
    void removeDirectedEdgeFromPatternLanguage(UUID patternLanguageId, UUID directedEdgeId);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_EDIT)")
    UndirectedEdge createUndirectedEdgeAndAddToPatternLanguage(UUID patternLanguageId, CreateUndirectedEdgeRequest createUndirectedEdgeRequest);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_READ)")
    List<UndirectedEdge> getUndirectedEdgesOfPatternLanguage(UUID patternLanguageId);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_READ)")
    UndirectedEdge getUndirectedEdgeOfPatternLanguageById(UUID patternLanguageId, UUID undirectedEdgeId) throws UndirectedEdgeNotFoundException;

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_EDIT)")
    UndirectedEdge updateUndirectedEdgeOfPatternLanguage(UUID patternLanguageId, UndirectedEdge undirectedEdge);

    @PreAuthorize(value = "hasResourcePermission(#patternLanguageId, @PC.PATTERN_LANGUAGE_EDIT)")
    void removeUndirectedEdgeFromPatternLanguage(UUID patternLanguageId, UUID undirectedEdgeId);
}
