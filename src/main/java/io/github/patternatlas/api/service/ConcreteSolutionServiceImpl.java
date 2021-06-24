package io.github.patternatlas.api.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.github.patternatlas.api.exception.AggregationException;
import io.github.patternatlas.api.exception.ConcreteSolutionNotFoundException;
import io.github.patternatlas.api.entities.designmodel.AggregationData;
import io.github.patternatlas.api.entities.designmodel.AggregationDataAndPatternEdge;
import io.github.patternatlas.api.entities.designmodel.ConcreteSolution;
import io.github.patternatlas.api.entities.designmodel.DesignModelEdgeType;
import io.github.patternatlas.api.entities.designmodel.DesignModelPatternEdge;
import io.github.patternatlas.api.entities.designmodel.DesignModelPatternInstance;
import io.github.patternatlas.api.repositories.ConcreteSolutionRepository;
import io.github.patternatlas.api.repositories.DesignModelEdgeTypeRepository;
import io.github.patternatlas.api.rest.model.FileDTO;
import io.github.patternatlas.api.util.aggregator.Aggregator;
import io.github.patternatlas.api.util.aggregator.AggregatorScanner;
import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class ConcreteSolutionServiceImpl implements ConcreteSolutionService {

    private final ConcreteSolutionRepository concreteSolutionRepository;
    private final DesignModelEdgeTypeRepository designModelEdgeTypeRepository;

    private UUID lastSourceNodeUUID = null;

    public ConcreteSolutionServiceImpl(ConcreteSolutionRepository concreteSolutionRepository,
                                       DesignModelEdgeTypeRepository designModelEdgeTypeRepository) {
        this.concreteSolutionRepository = concreteSolutionRepository;
        this.designModelEdgeTypeRepository = designModelEdgeTypeRepository;
    }

    public List<ConcreteSolution> getConcreteSolutions() {
        return this.concreteSolutionRepository.findAll();
    }

    public List<ConcreteSolution> getConcreteSolutions(URI patternUri) {
        return this.concreteSolutionRepository.findAllByPatternUri(patternUri.toString());
    }

    public ConcreteSolution getConcreteSolution(UUID uuid) {
        return this.concreteSolutionRepository.findTopById(uuid)
                .orElseThrow(() -> new ConcreteSolutionNotFoundException(uuid));
    }

    private void linkConcreteSolutionsToPatternInstances(List<DesignModelPatternInstance> patternInstances, Map<UUID, UUID> concreteSolutionMapping) {
        for (DesignModelPatternInstance patternInstance : patternInstances) {
            UUID piId = patternInstance.getPatternInstanceId();
            UUID csId = concreteSolutionMapping.get(piId);
            ConcreteSolution cs = this.concreteSolutionRepository.findTopById(csId)
                    .orElseThrow(() -> new ConcreteSolutionNotFoundException(csId));
            patternInstance.setConcreteSolution(cs);
        }
    }

    private void normalizeEdgeDirections(List<DesignModelPatternEdge> edges) {
        Set<String> edgeTypesToSwapDirections = this.designModelEdgeTypeRepository.findAll().stream()
                .filter(DesignModelEdgeType::getSwap)
                .map(DesignModelEdgeType::getName)
                .collect(Collectors.toSet());

        for (DesignModelPatternEdge edge : edges) {
            if (edge.isDirectedEdge() && edgeTypesToSwapDirections.contains(edge.getType())) {
                DesignModelPatternInstance source = edge.getPatternInstance1();
                edge.setPatternInstance1(edge.getPatternInstance2());
                edge.setPatternInstance2(source);
            }
        }
    }

    private Set<UUID> findSourceNodes(List<DesignModelPatternInstance> patternInstances, List<DesignModelPatternEdge> edges) {
        Set<UUID> sourceNodes = new HashSet<>();

        for (DesignModelPatternInstance patternInstance : patternInstances) {
            sourceNodes.add(patternInstance.getPatternInstanceId());
        }

        for (DesignModelPatternEdge edge : edges) {
            sourceNodes.remove(edge.getPatternInstance2().getPatternInstanceId());
        }

        return sourceNodes;
    }

    private AggregationDataAndPatternEdge getSourceNodeAndNeighbor(List<DesignModelPatternInstance> patternInstances, List<DesignModelPatternEdge> edges) {
        Set<UUID> sourceNodes = findSourceNodes(patternInstances, edges);

        if (sourceNodes.isEmpty()) {
            throw new RuntimeException("No source node found");
        }

        final UUID sourceNodeUUID = sourceNodes.contains(lastSourceNodeUUID) ? lastSourceNodeUUID : sourceNodes.iterator().next();
        lastSourceNodeUUID = sourceNodeUUID;

        DesignModelPatternInstance sourcePatternInstance = patternInstances.stream()
                .filter(designModelPatternInstance -> sourceNodeUUID.equals(designModelPatternInstance.getPatternInstanceId()))
                .findAny().orElse(null);

        if (patternInstances.size() == 1) {
            return new AggregationDataAndPatternEdge(new AggregationData(sourcePatternInstance, null), null);
        }

        DesignModelPatternEdge edge = edges.stream().filter(designModelPatternEdge -> sourceNodeUUID.equals(designModelPatternEdge.getPatternInstance1().getPatternInstanceId())).findAny().orElse(null);

        DesignModelPatternInstance neighborPatternInstance = null;

        if (edge != null) {
            UUID neighborNodeUUID = edge.getPatternInstance2().getPatternInstanceId();

            neighborPatternInstance = patternInstances.stream()
                    .filter(designModelPatternInstance -> neighborNodeUUID.equals(designModelPatternInstance.getPatternInstanceId()))
                    .findAny().orElse(null);

            log.info("Found source [" + sourceNodeUUID.toString() + "] and neighbor [" + neighborNodeUUID.toString() + "]: " + sourcePatternInstance.getPattern().getName() + " ---" + edge.getType() + "--- " + neighborPatternInstance.getPattern().getName());
        }

        return new AggregationDataAndPatternEdge(new AggregationData(sourcePatternInstance, neighborPatternInstance), edge);
    }

    private void aggregate(AggregationData aggregationData) {

        String sourceAggregationType = aggregationData.getSource().getConcreteSolution().getAggregatorType();
        String targetAggregationType = null;
        try {
            targetAggregationType = aggregationData.getTarget().getConcreteSolution().getAggregatorType();
        } catch (NullPointerException ignored) {
        }

        Aggregator aggregator = AggregatorScanner.findMatchingAggregatorImpl(sourceAggregationType, targetAggregationType);

        if (aggregator == null) {
            throw new AggregationException("Aggregation type combination is not yet supported: [" + sourceAggregationType + "] --> [" + targetAggregationType + "]");
        }

        try {
            aggregator.aggregate(aggregationData);
        } catch (AggregationException e) {
            throw new AggregationException("Failed to aggregate concrete solutions");
        }
    }

    public List<FileDTO> aggregate(List<DesignModelPatternInstance> patternInstances, List<DesignModelPatternEdge> edges, Map<UUID, UUID> concreteSolutionMapping) {

        linkConcreteSolutionsToPatternInstances(patternInstances, concreteSolutionMapping);

        normalizeEdgeDirections(edges);

        Map<String, Object> templateContext = new HashMap<>();
        List<FileDTO> artefacts = new ArrayList<>();

        while (!patternInstances.isEmpty()) {
            AggregationDataAndPatternEdge aggregationDataAndPatternEdge = getSourceNodeAndNeighbor(patternInstances, edges);
            AggregationData aggregationData = aggregationDataAndPatternEdge.getAggregationData();

            aggregationData.setTemplateContext(templateContext);

            aggregate(aggregationData);

            templateContext = aggregationData.getTemplateContext();

            if (aggregationData.getResult() != null) {
                artefacts.add(aggregationData.getResult());
            }

            edges.remove(aggregationDataAndPatternEdge.getEdge());
            if (!edges.stream().anyMatch(edge -> aggregationData.getSource().getPatternInstanceId().equals(edge.getPatternInstance1().getPatternInstanceId()))) {
                patternInstances.remove(aggregationData.getSource());
            }
        }

        return artefacts;
    }
}
