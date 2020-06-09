package com.patternpedia.api.service;

import com.patternpedia.api.entities.designmodel.DesignModel;

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

    void addPatternToDesignModel(UUID designModelId, UUID patternId);

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
