package com.patternpedia.api.service;

import com.patternpedia.api.entities.designmodel.ConcreteSolution;
import com.patternpedia.api.entities.designmodel.DesignModelPatternEdge;
import com.patternpedia.api.entities.designmodel.DesignModelPatternEdgeId;
import com.patternpedia.api.entities.designmodel.DesignModelPatternInstance;
import com.patternpedia.api.exception.ConcreteSolutionNotFoundException;
import com.patternpedia.api.repositories.ConcreteSolutionRepository;
import com.patternpedia.api.rest.model.FileDTO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


@Service
@CommonsLog
public class ConcreteSolutionServiceImpl implements ConcreteSolutionService {

    private final ConcreteSolutionRepository concreteSolutionRepository;

    private static final Random RANDOM = new Random();


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


    public FileDTO aggregate(List<DesignModelPatternInstance> patternInstances, List<DesignModelPatternEdge> edges, String technology) {
        Set<String> patternUriSet = patternInstances.stream().map(i -> i.getPattern().getUri()).collect(Collectors.toSet());
        Map<String, ConcreteSolution> patternToConcreteSolutionMapping = new HashMap<>();
        patternUriSet.forEach(uri -> patternToConcreteSolutionMapping.put(
                uri, this.concreteSolutionRepository.findTopByPatternUriAndAggregatorType(uri, technology).get()
        ));

        Map<UUID, String> patternInstanceImplementations = new HashMap<>();
        patternInstances.forEach(instance -> {
            ConcreteSolution concreteSolution = patternToConcreteSolutionMapping.get(instance.getPattern().getUri());

            patternInstanceImplementations.put(instance.getPatternInstanceId(), readFile(concreteSolution.getTemplateRef()));
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

        String mainTemplate = readFile(technologyWrapper.get(technology));
        String camelXML = renderTemplate(mainTemplate, Collections.singletonMap("camelContext", camelContext.toString()));

        String name = "";
        String mime = "";
        switch (technology) {
            case "ActiveMQ-XML":
                name = "camel.xml";
                mime = "text/xml";
                break;
            case "ActiveMQ-Java":
                name = "PatternAtlasRouteBuilder.java";
                mime = "text/x-java";
                break;
        }

        return new FileDTO(name, mime, camelXML);
    }


    private static String readFile(String url) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            return reader.lines().collect(Collectors.joining("\n"));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    private static Object getQueueList(Collection<DesignModelPatternEdge> edges) {
        if (edges.size() == 1) {
            DesignModelPatternEdgeId edgeId = edges.iterator().next().getEdgeId();
            return "queue" + edgeId.getPatternInstanceId1().toString() + edgeId.getPatternInstanceId2().toString();
        }
        if (edges.size() >= 2) {
            List<String> queueNames = new ArrayList<>();
            for (DesignModelPatternEdge edge : edges) {
                DesignModelPatternEdgeId edgeId = edge.getEdgeId();
                queueNames.add("queue" + edgeId.getPatternInstanceId1().toString() + edgeId.getPatternInstanceId2().toString());
            }
            return queueNames;
        }
        return null;
    }


    private static String renderTemplate(String concreteSolutionTemplate, Map<String, Object> dataContainer) {
        ST template = new ST(concreteSolutionTemplate, '$', '$');

        template.add("random", RANDOM.nextInt(Integer.MAX_VALUE));

        for (String key : dataContainer.keySet()) {
            template.add(key, dataContainer.get(key));
        }

        return template.render();
    }
}
