package com.patternpedia.api.service;

import com.patternpedia.api.entities.designmodel.AggregationData;
import com.patternpedia.api.entities.designmodel.ConcreteSolution;
import com.patternpedia.api.entities.designmodel.DesignModelPatternEdge;
import com.patternpedia.api.entities.designmodel.DesignModelPatternInstance;
import com.patternpedia.api.exception.ConcreteSolutionNotFoundException;
import com.patternpedia.api.repositories.ConcreteSolutionRepository;
import com.patternpedia.api.rest.model.FileDTO;
import com.patternpedia.api.util.aggregator.Aggregator;
import com.patternpedia.api.util.aggregator.AggregatorScanner;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@CommonsLog
public class ConcreteSolutionServiceImpl implements ConcreteSolutionService {

    private final ConcreteSolutionRepository concreteSolutionRepository;


    public ConcreteSolutionServiceImpl(ConcreteSolutionRepository concreteSolutionRepository) {
        this.concreteSolutionRepository = concreteSolutionRepository;
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


    private void swapEdgeDirections(List<DesignModelPatternEdge> edges) {
        Set<String> edgeTypesToSwapDirections = Stream.of("produce", "publish").collect(Collectors.toSet());

        for (DesignModelPatternEdge edge : edges) {
            if (edge.isDirectedEdge() && edgeTypesToSwapDirections.contains(edge.getType())) {
                DesignModelPatternInstance source = edge.getPatternInstance1();
                edge.setPatternInstance1(edge.getPatternInstance2());
                edge.setPatternInstance2(source);
            }
        }
    }


    private Set<UUID> findRootNodes(List<DesignModelPatternInstance> patternInstances, List<DesignModelPatternEdge> edges) {
        Set<UUID> rootNodes = new HashSet<>();

        for (DesignModelPatternInstance patternInstance : patternInstances) {
            rootNodes.add(patternInstance.getPatternInstanceId());
        }

        for (DesignModelPatternEdge edge : edges) {
            rootNodes.remove(edge.getPatternInstance1().getPatternInstanceId());
        }

        log.info("Root nodes: " + rootNodes.toString());
        return rootNodes;
    }


    private Set<UUID> findLeafNodes(List<DesignModelPatternInstance> patternInstances, List<DesignModelPatternEdge> edges) {
        Set<UUID> leafNodes = new HashSet<>();

        for (DesignModelPatternInstance patternInstance : patternInstances) {
            leafNodes.add(patternInstance.getPatternInstanceId());
        }

        for (DesignModelPatternEdge edge : edges) {
            leafNodes.remove(edge.getPatternInstance2().getPatternInstanceId());
        }

        log.info("Leaf nodes: " + leafNodes.toString());
        return leafNodes;
    }


    private UUID lastLeafUUID = null;

    private AggregationData getLeafAndPredecessor(List<DesignModelPatternInstance> patternInstances, List<DesignModelPatternEdge> edges) {
        Set<UUID> leafNodes = findLeafNodes(patternInstances, edges);

        if (leafNodes.isEmpty()) {
            throw new RuntimeException("No leaf node found");
        }

        final UUID leafNodeUUID = leafNodes.contains(lastLeafUUID) ? lastLeafUUID : leafNodes.iterator().next();
        lastLeafUUID = leafNodeUUID;

        DesignModelPatternInstance leafPatternInstance = patternInstances.stream().filter(designModelPatternInstance -> leafNodeUUID.equals(designModelPatternInstance.getPatternInstanceId())).findAny().get();

        if (patternInstances.size() == 1) {
            return new AggregationData(leafPatternInstance, null, null);
        }

        DesignModelPatternEdge edge = edges.stream().filter(designModelPatternEdge -> leafNodeUUID.equals(designModelPatternEdge.getPatternInstance1().getPatternInstanceId())).findAny().orElse(null);

        DesignModelPatternInstance predecessorPatternInstance = null;

        if (edge != null) {
            UUID predecessorNodeUUID = edge.getPatternInstance2().getPatternInstanceId();

            predecessorPatternInstance = patternInstances.stream().filter(designModelPatternInstance -> predecessorNodeUUID.equals(designModelPatternInstance.getPatternInstanceId())).findAny().get();

            log.info("Found leaf [" + leafNodeUUID.toString() + "] and predecessor [" + predecessorNodeUUID.toString() + "]: " + leafPatternInstance.getPattern().getName() + " ---" + edge.getType() + "--> " + predecessorPatternInstance.getPattern().getName());
        }

        return new AggregationData(leafPatternInstance, predecessorPatternInstance, edge);
    }


    private void aggregate(AggregationData aggregationData) {

        String sourceAggregationType = aggregationData.getSource().getConcreteSolution().getAggregatorType();
        String targetAggregationType = null;
        try {
            targetAggregationType = aggregationData.getTarget().getConcreteSolution().getAggregatorType();
        } catch (NullPointerException e) {
        }

        Aggregator aggregator = AggregatorScanner.findMatchingAggregatorImpl(sourceAggregationType, targetAggregationType);

        if (aggregator == null) {
            throw new RuntimeException("Aggregation type combination is not yet supported: [" + sourceAggregationType + "] --> [" + targetAggregationType + "]");
        }

        aggregator.aggregate(aggregationData);
    }


    public List<FileDTO> aggregate(List<DesignModelPatternInstance> patternInstances, List<DesignModelPatternEdge> edges, Map<UUID, UUID> concreteSolutionMapping) {

        linkConcreteSolutionsToPatternInstances(patternInstances, concreteSolutionMapping);

        swapEdgeDirections(edges);

        Map<String, Object> templateContext = new HashMap<>();
        List<FileDTO> aggregatedFiles = new ArrayList<>();

        while (!patternInstances.isEmpty()) {
            AggregationData aggregationData = getLeafAndPredecessor(patternInstances, edges);

            aggregationData.setTemplateContext(templateContext);

            aggregate(aggregationData);

            templateContext = aggregationData.getTemplateContext();

            if (aggregationData.getResult() != null) {
                aggregatedFiles.add(aggregationData.getResult());
            }

            edges.remove(aggregationData.getEdge());
            if (!edges.stream().filter(edge -> aggregationData.getSource().getPatternInstanceId().equals(edge.getPatternInstance1().getPatternInstanceId())).findAny().isPresent()) {
                patternInstances.remove(aggregationData.getSource());
            }
        }


        return aggregatedFiles;
    }
}
