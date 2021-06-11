package com.patternatlas.api.service;

import com.patternatlas.api.entities.designmodel.DesignModel;
import com.patternatlas.api.entities.designmodel.DesignModelEdgeType;
import com.patternatlas.api.entities.designmodel.DesignModelPatternEdge;
import com.patternatlas.api.entities.designmodel.DesignModelPatternInstance;

import java.util.List;
import java.util.UUID;

public interface DesignModelService {

    DesignModel createDesignModel(DesignModel designModel);

    List<DesignModel> getAllDesignModels();

    DesignModel getDesignModel(UUID designModelId);

    List<DesignModelEdgeType> getDesignModelEdgeTypes();

    void addPatternInstance(UUID designModelId, UUID patternId);

    void deletePatternInstance(UUID designModelId, UUID patternInstanceId);

    DesignModelPatternInstance getPatternInstance(UUID designModelId, UUID patternInstanceId);

    void updatePatternInstancePosition(UUID designModelId, UUID patternInstanceId, Double x, Double y);

    List<DesignModelPatternEdge> getEdges(UUID designModelId);

    void addEdge(UUID designModelId, UUID patternInstanceId1, UUID patternInstanceId2, Boolean directed, String type, String description);

    void deleteEdge(UUID designModelId, UUID patternInstanceId1, UUID patternInstanceId2);

    void deleteDesignModel(UUID designModelId);
}
