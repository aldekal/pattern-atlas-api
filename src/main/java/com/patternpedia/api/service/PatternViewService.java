package com.patternpedia.api.service;

import java.util.List;
import java.util.UUID;

import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternView;
import com.patternpedia.api.entities.UndirectedEdge;

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

    DirectedEdge createDirectedEdgeAndAddToPatternView(UUID patternViewId, DirectedEdge directedEdge);

    List<DirectedEdge> getDirectedEdgesByPatternViewId(UUID patternViewId);

    DirectedEdge getDirectedEdgeOfPatternViewById(UUID patternViewId, UUID directedEdgeId);

    DirectedEdge updateDirectedEdgeOfPatternView(UUID patternViewId, DirectedEdge directedEdge);

    void removeDirectedEdgeFromPatternView(UUID patternViewId, UUID directedEdgeId);

    void addUndirectedEdgeToPatternView(UUID patternViewId, UUID undirectedEdgeId);

    UndirectedEdge createUndirectedEdgeAndAddToPatternView(UUID patternViewId, UndirectedEdge undirectedEdge);

    List<UndirectedEdge> getUndirectedEdgesByPatternViewId(UUID patternViewId);

    UndirectedEdge getUndirectedEdgeOfPatternViewById(UUID patternViewId, UUID undirectedEdgeId);

    UndirectedEdge updateUndirectedEdgeOfPatternView(UUID patternViewId, UndirectedEdge undirectedEdge);

    void removeUndirectedEdgeFromPatternView(UUID patternViewId, UUID undirectedEdgeId);
}
