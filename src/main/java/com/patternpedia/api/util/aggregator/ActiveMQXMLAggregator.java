package com.patternpedia.api.util.aggregator;

import com.patternpedia.api.entities.designmodel.ConcreteSolution;
import com.patternpedia.api.entities.designmodel.DesignModelPatternEdge;
import com.patternpedia.api.entities.designmodel.DesignModelPatternInstance;
import lombok.extern.apachecommons.CommonsLog;

import java.util.*;
import java.util.stream.Collectors;


@CommonsLog
public class ActiveMQXMLAggregator extends ActiveMQAggregator {

    private static final String TECHNOLOGY = "ActiveMQ-XML";


    @Override
    public String aggregate(List<DesignModelPatternInstance> patternInstances, List<DesignModelPatternEdge> edges, Map<String, Object> query) {

        Set<String> patternUriSet = patternInstances.stream().map(i -> i.getPattern().getUri()).collect(Collectors.toSet());
        Map<String, ConcreteSolution> patternToConcreteSolutionMapping = new HashMap<>();
        patternUriSet.forEach(uri -> {
            try {
//                patternToConcreteSolutionMapping.put(
//                        uri, this.concreteSolutionRepository.findTopByPatternUriAndAggregatorType(uri, TECHNOLOGY).get()
//                );
            } catch (NoSuchElementException e) {
                log.info("No concrete solution found for " + uri);
            }
        });

        Map<UUID, String> patternInstanceImplementations = new HashMap<>();
        patternInstances.forEach(instance -> {
            ConcreteSolution concreteSolution = patternToConcreteSolutionMapping.get(instance.getPattern().getUri());

            if (concreteSolution != null) {
                patternInstanceImplementations.put(instance.getPatternInstanceId(), readFile(concreteSolution.getTemplateRef()));
            }
        });

        StringBuilder camelContext = new StringBuilder();

        patternInstanceImplementations.keySet().forEach(uuid -> {
            List<DesignModelPatternEdge> incomingEdges = edges.stream().filter(edge -> uuid.equals(edge.getEdgeId().getPatternInstanceId2())).collect(Collectors.toList());
            List<DesignModelPatternEdge> outgoingEdges = edges.stream().filter(edge -> uuid.equals(edge.getEdgeId().getPatternInstanceId1())).collect(Collectors.toList());

            Map<String, Object> dataContainer = new HashMap<>();
            dataContainer.put("input", getQueueList(incomingEdges));
            dataContainer.put("output", getQueueList(outgoingEdges));

            camelContext.append(renderTemplate(patternInstanceImplementations.get(uuid), dataContainer));
            camelContext.append("\n");
        });

        Map<String, String> technologyWrapper = new HashMap<>();
        technologyWrapper.put("ActiveMQ-XML", "file:///home/marcel/Dokumente/Studium Softwaretechnik/Vorlesungen/14. Semester/Masterthesis/Pattern Atlas/concrete-solutions/eip-activemq-xml/camel.st");
        technologyWrapper.put("ActiveMQ-Java", "file:///home/marcel/Dokumente/Studium Softwaretechnik/Vorlesungen/14. Semester/Masterthesis/Pattern Atlas/concrete-solutions/eip-activemq-java/camel.st");

        String mainTemplate = readFile(technologyWrapper.get(TECHNOLOGY));
        String camelXML = renderTemplate(mainTemplate, Collections.singletonMap("camelContext", camelContext.toString()));

        return camelXML;
    }
}
