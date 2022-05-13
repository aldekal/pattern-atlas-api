package io.github.patternatlas.api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import io.github.patternatlas.api.entities.DirectedEdge;
import io.github.patternatlas.api.entities.Pattern;
import io.github.patternatlas.api.entities.PatternView;
import io.github.patternatlas.api.entities.UndirectedEdge;
import io.github.patternatlas.api.rest.model.AddDirectedEdgeToViewRequest;
import io.github.patternatlas.api.rest.model.AddUndirectedEdgeToViewRequest;
import io.github.patternatlas.api.rest.model.UpdateDirectedEdgeRequest;
import io.github.patternatlas.api.rest.model.UpdateUndirectedEdgeRequest;

public interface PatternViewService {

    @PreAuthorize(value = "hasGlobalPermission(@PC.PATTERN_VIEW_CREATE)")
    PatternView createPatternView(PatternView patternView);

    @PostFilter("hasResourcePermission(filterObject.id, @PC.PATTERN_VIEW_READ)")
    List<PatternView> getAllPatternViews();

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_READ)")
    PatternView getPatternViewById(UUID patternViewId);

    @PostAuthorize(value = "hasResourcePermission(returnObject.id, @PC.PATTERN_VIEW_READ)")
    PatternView getPatternViewByUri(String uri);

    @PreAuthorize(value = "hasResourcePermission(#patternView.id, @PC.PATTERN_VIEW_EDIT)")
    PatternView updatePatternView(PatternView patternView);

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_DELETE)")
    void deletePatternView(UUID patternViewId);

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_EDIT)")
    void addPatternToPatternView(UUID patternViewId, UUID patternId);

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_READ)")
    @PostFilter("hasResourcePermission(filterObject.id, @PC.APPROVED_PATTERN_READ)")
    List<Pattern> getPatternsOfPatternView(UUID patternViewId);

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_READ) && hasResourcePermission(#patternId, @PC.APPROVED_PATTERN_READ)")
    Pattern getPatternOfPatternViewById(UUID patternViewId, UUID patternId);

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_EDIT)")
    void removePatternFromPatternView(UUID patternViewId, UUID patternId);

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_EDIT)")
    void addDirectedEdgeToPatternView(UUID patternViewId, UUID directedEdgeId);

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_EDIT)")
    DirectedEdge createDirectedEdgeAndAddToPatternView(UUID patternViewId, AddDirectedEdgeToViewRequest request);

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_READ)")
    List<DirectedEdge> getDirectedEdgesByPatternViewId(UUID patternViewId);

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_READ)")
    DirectedEdge getDirectedEdgeOfPatternViewById(UUID patternViewId, UUID directedEdgeId);

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_EDIT)")
    DirectedEdge updateDirectedEdgeOfPatternView(UUID patternViewId, UpdateDirectedEdgeRequest request);

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_EDIT)")
    void removeDirectedEdgeFromPatternView(UUID patternViewId, UUID directedEdgeId);

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_EDIT)")
    void addUndirectedEdgeToPatternView(UUID patternViewId, UUID undirectedEdgeId);

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_EDIT)")
    UndirectedEdge createUndirectedEdgeAndAddToPatternView(UUID patternViewId, AddUndirectedEdgeToViewRequest request);

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_READ)")
    List<UndirectedEdge> getUndirectedEdgesByPatternViewId(UUID patternViewId);

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_READ)")
    UndirectedEdge getUndirectedEdgeOfPatternViewById(UUID patternViewId, UUID undirectedEdgeId);

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_EDIT)")
    UndirectedEdge updateUndirectedEdgeOfPatternView(UUID patternViewId, UpdateUndirectedEdgeRequest request);

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_EDIT)")
    void removeUndirectedEdgeFromPatternView(UUID patternViewId, UUID undirectedEdgeId);

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_READ)")
    Object getGraphOfPatternView(UUID patternViewId);

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_EDIT)")
    Object createGraphOfPatternView(UUID patternViewId, Object graph);

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_EDIT)")
    Object updateGraphOfPatternView(UUID patternViewId, Object graph);

    @PreAuthorize(value = "hasResourcePermission(#patternViewId, @PC.PATTERN_VIEW_EDIT)")
    void deleteGraphOfPatternView(UUID patternViewId);
}
