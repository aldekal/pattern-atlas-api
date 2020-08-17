package com.patternpedia.api.service;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.designmodel.DesignModel;
import com.patternpedia.api.entities.designmodel.DesignModelPatternEdge;
import com.patternpedia.api.entities.designmodel.DesignModelPatternInstance;

import java.util.List;
import java.util.UUID;

public interface DesignModelService {

    DesignModel createDesignModel(DesignModel designModel);

    List<DesignModel> getAllDesignModels();

    DesignModel getDesignModel(UUID designModelId);

//    DesignModel getDesignModelByUri(String uri);
//
//    DesignModel updateDesignModel(DesignModel designModel);
//
//    void deleteDesignModel(UUID designModelId);

    void addPatternInstance(UUID designModelId, UUID patternId);

    void deletePatternInstance(UUID designModelId, UUID patternInstanceId);

    DesignModelPatternInstance getPatternInstance(UUID designModelId, UUID patternInstanceId);

    void updatePatternInstance(UUID designModelId, UUID patternInstanceId, Pattern pattern);

    void updatePatternInstancePosition(UUID designModelId, UUID patternInstanceId, Double x, Double y);

//    void addEdge(UUID designModelId, DesignModelEdge edge);

    List<DesignModelPatternEdge> getEdges(UUID designModelId);

    void addEdge(UUID designModelId, UUID patternInstanceId1, UUID patternInstanceId2, Boolean directed, String type, String description);

    void deleteEdge(UUID designModelId, UUID patternInstanceId1, UUID patternInstanceId2);


//    List<Pattern> getPatternsOfDesignModel(UUID designModelId);
//
//    Pattern getPatternOfDesignModelById(UUID designModelId, UUID patternId);
//
//    void removePatternFromDesignModel(UUID designModelId, UUID patternId);
//
//    void addDirectedEdgeToDesignModel(UUID designModelId, UUID directedEdgeId);
//
//    DirectedEdge createDirectedEdgeAndAddToDesignModel(UUID designModelId, AddDirectedEdgeToViewRequest request);
//
//    List<DirectedEdge> getDirectedEdgesByDesignModelId(UUID designModelId);
//
//    DirectedEdge getDirectedEdgeOfDesignModelById(UUID designModelId, UUID directedEdgeId);
//
//    DirectedEdge updateDirectedEdgeOfDesignModel(UUID designModelId, UpdateDirectedEdgeRequest request);
//
//    void removeDirectedEdgeFromDesignModel(UUID designModelId, UUID directedEdgeId);
//
//    void addUndirectedEdgeToDesignModel(UUID designModelId, UUID undirectedEdgeId);
//
//    UndirectedEdge createUndirectedEdgeAndAddToDesignModel(UUID designModelId, AddUndirectedEdgeToViewRequest request);
//
//    List<UndirectedEdge> getUndirectedEdgesByDesignModelId(UUID designModelId);
//
//    UndirectedEdge getUndirectedEdgeOfDesignModelById(UUID designModelId, UUID undirectedEdgeId);
//
//    UndirectedEdge updateUndirectedEdgeOfDesignModel(UUID designModelId, UpdateUndirectedEdgeRequest request);
//
//    void removeUndirectedEdgeFromDesignModel(UUID designModelId, UUID undirectedEdgeId);
}
