package com.patternatlas.api.service;

import java.util.List;
import java.util.UUID;

import com.patternatlas.api.entities.DirectedEdge;
import com.patternatlas.api.entities.Pattern;
import com.patternatlas.api.entities.PatternView;
import com.patternatlas.api.entities.UndirectedEdge;
import com.patternatlas.api.rest.model.AddDirectedEdgeToViewRequest;
import com.patternatlas.api.rest.model.AddUndirectedEdgeToViewRequest;
import com.patternatlas.api.rest.model.UpdateDirectedEdgeRequest;
import com.patternatlas.api.rest.model.UpdateUndirectedEdgeRequest;
import org.springframework.transaction.annotation.Transactional;

public interface PatternViewService {

    PatternView createPatternView(PatternView patternView);

    List<PatternView> getAllPatternViews();

    PatternView getPatternViewById(UUID patternViewId);

    PatternView getPatternViewByUri(String uri);

    PatternView updatePatternView(PatternView patternView);

    void deletePatternView(UUID patternViewId);

    void addPatternToPatternView(UUID patternViewId, UUID patternId);

    List<Pattern> getPatternsOfPatternView(UUID patternViewId);

    Pattern getPatternOfPatternViewById(UUID patternViewId, UUID patternId);

    void removePatternFromPatternView(UUID patternViewId, UUID patternId);

    void addDirectedEdgeToPatternView(UUID patternViewId, UUID directedEdgeId);

    DirectedEdge createDirectedEdgeAndAddToPatternView(UUID patternViewId, AddDirectedEdgeToViewRequest request);

    List<DirectedEdge> getDirectedEdgesByPatternViewId(UUID patternViewId);

    DirectedEdge getDirectedEdgeOfPatternViewById(UUID patternViewId, UUID directedEdgeId);

    DirectedEdge updateDirectedEdgeOfPatternView(UUID patternViewId, UpdateDirectedEdgeRequest request);

    void removeDirectedEdgeFromPatternView(UUID patternViewId, UUID directedEdgeId);

    void addUndirectedEdgeToPatternView(UUID patternViewId, UUID undirectedEdgeId);

    UndirectedEdge createUndirectedEdgeAndAddToPatternView(UUID patternViewId, AddUndirectedEdgeToViewRequest request);

    List<UndirectedEdge> getUndirectedEdgesByPatternViewId(UUID patternViewId);

    UndirectedEdge getUndirectedEdgeOfPatternViewById(UUID patternViewId, UUID undirectedEdgeId);

    UndirectedEdge updateUndirectedEdgeOfPatternView(UUID patternViewId, UpdateUndirectedEdgeRequest request);

    void removeUndirectedEdgeFromPatternView(UUID patternViewId, UUID undirectedEdgeId);

    @Transactional(readOnly = true)
    Object getGraphOfPatternView(UUID patternLanguageId);

    @Transactional
    Object createGraphOfPatternView(UUID patternLanguageId, Object graph);

    @Transactional
    Object updateGraphOfPatternView(UUID patternLanguageId, Object graph);

    @Transactional
    void deleteGraphOfPatternView(UUID patternLanguageId);
}
