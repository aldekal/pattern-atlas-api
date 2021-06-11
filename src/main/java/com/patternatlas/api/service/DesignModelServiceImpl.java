package com.patternatlas.api.service;

import com.patternatlas.api.entities.Pattern;
import com.patternatlas.api.entities.designmodel.*;
import com.patternatlas.api.exception.DesignModelNotFoundException;
import com.patternatlas.api.exception.DesignModelPatternInstanceNotFoundException;
import com.patternatlas.api.exception.NullDesignModelException;
import com.patternatlas.api.repositories.*;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DesignModelServiceImpl implements DesignModelService {

    private PatternService patternService;
    private DesignModelRepository designModelRepository;
    private DesignModelPatternInstanceRepository designModelPatternInstanceRepository;
    private DesignModelPatternEdgeRepository designModelPatternEdgeRepository;
    private DesignModelEdgeTypeRepository designModelEdgeTypeRepository;


    public DesignModelServiceImpl(PatternService patternService,
                                  PatternRelationDescriptorService patternRelationDescriptorService,
                                  DesignModelRepository designModelRepository,
                                  DesignModelPatternInstanceRepository designModelPatternInstanceRepository,
                                  DesignModelPatternEdgeRepository designModelPatternEdgeRepository,
                                  PatternViewDirectedEdgeRepository patternViewDirectedEdgeRepository,
                                  PatternViewUndirectedEdgeRepository patternViewUndirectedEdgeRepository,
                                  DirectedEdgeRepository directedEdgeRepository,
                                  UndirectedEdgeReository undirectedEdgeReository,
                                  DesignModelEdgeTypeRepository designModelEdgeTypeRepository) {
        this.patternService = patternService;
        this.designModelRepository = designModelRepository;
        this.designModelPatternInstanceRepository = designModelPatternInstanceRepository;
        this.designModelPatternEdgeRepository = designModelPatternEdgeRepository;
        this.designModelEdgeTypeRepository = designModelEdgeTypeRepository;
    }


    @Override
    @Transactional
    public DesignModel createDesignModel(DesignModel designModel) {
        if (null == designModel) {
            throw new NullDesignModelException();
        }

        return this.designModelRepository.save(designModel);
    }


    @Override
    @Transactional(readOnly = true)
    public List<DesignModel> getAllDesignModels() {
        try {
            return this.designModelRepository.findAll();
        } catch (InvalidDataAccessResourceUsageException e) {
            return Collections.emptyList();
        }
    }


    @Override
    @Transactional(readOnly = true)
    public DesignModel getDesignModel(UUID designModelId) {
        DesignModel designModel = this.designModelRepository.findById(designModelId)
                .orElseThrow(() -> new DesignModelNotFoundException(designModelId));

        return designModel;
    }


    public List<DesignModelEdgeType> getDesignModelEdgeTypes() {
        return this.designModelEdgeTypeRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public DesignModelPatternInstance getPatternInstance(UUID designModelId, UUID patternInstanceId) {
        DesignModelPatternInstance patternInstance = this.designModelPatternInstanceRepository
                .findTopByDesignModel_IdAndPatternInstanceId(designModelId, patternInstanceId)
                .orElseThrow(() -> new DesignModelPatternInstanceNotFoundException(designModelId, patternInstanceId));

        return patternInstance;
    }


    @Override
    @Transactional
    public void addPatternInstance(UUID patternViewId, UUID patternId) {
        DesignModel designModel = this.getDesignModel(patternViewId);
        Pattern pattern = this.patternService.getPatternById(patternId);

        DesignModelPatternInstance designModelPattern = new DesignModelPatternInstance(designModel, pattern);
        this.designModelPatternInstanceRepository.save(designModelPattern);
    }


    @Override
    @Transactional
    public void deletePatternInstance(UUID designModelId, UUID patternInstanceId) {
        this.designModelPatternEdgeRepository.deleteAllByDesignModel_IdAndPatternInstance1_PatternInstanceIdOrPatternInstance2_PatternInstanceId(
                designModelId, patternInstanceId, patternInstanceId
        );
        this.designModelPatternInstanceRepository.deleteAllByDesignModel_IdAndPatternInstanceId(designModelId, patternInstanceId);
    }


    @Override
    @Transactional
    public void updatePatternInstancePosition(UUID designModelId, UUID patternInstanceId, Double x, Double y) {
        DesignModelPatternInstance patternInstance = this.designModelPatternInstanceRepository
                .findTopByDesignModel_IdAndPatternInstanceId(designModelId, patternInstanceId)
                .orElseThrow(() -> new DesignModelPatternInstanceNotFoundException(designModelId, patternInstanceId));

        DesignModelPatternGraphData graphData = new DesignModelPatternGraphData();
        graphData.setX(x);
        graphData.setY(y);

        patternInstance.setGraphData(graphData);

        this.designModelPatternInstanceRepository.save(patternInstance);
    }


    // DirectedEdge Handling
    @Override
    @Transactional(readOnly = true)
    public List<DesignModelPatternEdge> getEdges(UUID designModelId) {
        List<DesignModelPatternEdge> edgeList = this.designModelPatternEdgeRepository.findAllByDesignModelId(designModelId).orElse(Collections.emptyList());

        if (edgeList.isEmpty() && !this.designModelRepository.existsById(designModelId)) {
            throw new DesignModelNotFoundException(designModelId);
        }

        return edgeList;
    }


    @Override
    @Transactional
    public void addEdge(UUID designModelId, UUID patternInstanceId1, UUID patternInstanceId2, Boolean directed, String type, String description) {
        DesignModel designModel = this.designModelRepository.findById(designModelId)
                .orElseThrow(() -> new DesignModelNotFoundException(designModelId));

        DesignModelPatternInstance patternInstance1 = this.designModelPatternInstanceRepository
                .findTopByDesignModel_IdAndPatternInstanceId(designModelId, patternInstanceId1)
                .orElseThrow(() -> new DesignModelPatternInstanceNotFoundException(designModelId, patternInstanceId1));

        DesignModelPatternInstance patternInstance2 = this.designModelPatternInstanceRepository
                .findTopByDesignModel_IdAndPatternInstanceId(designModelId, patternInstanceId2)
                .orElseThrow(() -> new DesignModelPatternInstanceNotFoundException(designModelId, patternInstanceId2));

        DesignModelPatternEdge designModelEdge = new DesignModelPatternEdge();
        designModelEdge.setDesignModel(designModel);
        designModelEdge.setPatternInstance1(patternInstance1);
        designModelEdge.setPatternInstance2(patternInstance2);
        designModelEdge.setIsDirectedEdge(directed);
        designModelEdge.setType(type);
        designModelEdge.setDescription(description);

        this.designModelPatternEdgeRepository.save(designModelEdge);
    }


    @Override
    @Transactional
    public void deleteEdge(UUID designModelId, UUID patternInstanceId1, UUID patternInstanceId2) {
        this.designModelPatternEdgeRepository.deleteAllByDesignModel_IdAndPatternInstance1_PatternInstanceIdAndPatternInstance2_PatternInstanceId(
                designModelId, patternInstanceId1, patternInstanceId2
        );
    }

    @Override
    public void deleteDesignModel(UUID designModelId) {
        this.designModelRepository.deleteById(designModelId);
    }
}
