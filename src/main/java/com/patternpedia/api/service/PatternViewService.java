package com.patternpedia.api.service;

import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternView;
import com.patternpedia.api.entities.UndirectedEdge;

import java.util.List;
import java.util.UUID;

public interface PatternViewService {

    PatternView createPatternView(PatternView patternView);

    List<PatternView> getAllPatternViews();

    PatternView getPatternViewById(UUID patternViewId);

    PatternView getPatternViewByUri(String uri);

    PatternView updatePatternView(PatternView patternView);

    void deletePatternView(UUID patternViewId);

    List<Pattern> getAllPatternsOfPatternView(UUID patternViewId);

    Pattern getPatternOfPatternViewById(UUID patternViewId, UUID patternId);

    void removePatternFromPatternView(UUID patternViewId, UUID patternId);

    void addPatternToPatternView(UUID patternViewId, UUID patternId);

    List<DirectedEdge> getDirectedEdgesByPatternViewId(UUID patternViewId);

    DirectedEdge getDirectedEdgeOfPatternViewById(UUID patternViewId, UUID directedEdgeId);

    DirectedEdge createDirectedEdgeAndAddToPatternView(UUID patternViewId, DirectedEdge directedEdge);

    void addDirectedEdgeToPatternView(UUID patternViewId, UUID directedEdgeId);

    void removeDirectedEdgeFromPatternView(UUID patternViewId, UUID directedEdgeId);

    List<UndirectedEdge> getUndirectedEdgesByPatternViewId(UUID patternViewId);

    UndirectedEdge getUndirectedEdgeOfPatternViewById(UUID patternViewId, UUID undirectedEdgeId);

    UndirectedEdge createUndirectedEdgeAndAddToPatternView(UUID patternViewId, UndirectedEdge undirectedEdge);

    UndirectedEdge addUndirectedEdgeToPatternView(UUID patternViewId, UUID undirectedEdgeId);

    void removeUndirectedEdgeFromPatternView(UUID patternViewId, UUID undirectedEdgeId);
}
