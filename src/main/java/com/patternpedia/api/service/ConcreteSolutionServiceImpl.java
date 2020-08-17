package com.patternpedia.api.service;

import com.patternpedia.api.entities.designmodel.ConcreteSolution;
import com.patternpedia.api.entities.designmodel.DesignModelPatternEdge;
import com.patternpedia.api.entities.designmodel.DesignModelPatternInstance;
import com.patternpedia.api.exception.ConcreteSolutionNotFoundException;
import com.patternpedia.api.repositories.ConcreteSolutionRepository;
import com.patternpedia.api.rest.model.FileDTO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


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


    public List<FileDTO> aggregate(List<DesignModelPatternInstance> patternInstances, List<DesignModelPatternEdge> edges, Map<UUID, UUID> concreteSolutionMapping) {

        DesignModelPatternInstance patternWithHighestCSPrio = null;

        for (DesignModelPatternInstance patternInstance : patternInstances) {
            UUID piId = patternInstance.getPatternInstanceId();
            UUID csId = concreteSolutionMapping.get(piId);
            ConcreteSolution cs = this.concreteSolutionRepository.findTopById(csId)
                    .orElseThrow(() -> new ConcreteSolutionNotFoundException(csId));
            patternInstance.setConcreteSolution(cs);

            if (patternInstance.getConcreteSolution().getPriority() != null) {
                if (patternWithHighestCSPrio == null || patternInstance.getConcreteSolution().getPriority() > patternWithHighestCSPrio.getConcreteSolution().getPriority()) {
                    patternWithHighestCSPrio = patternInstance;
                }
            }
        }

        List<FileDTO> aggregatedFiles = new ArrayList<>();

//        for (String technology : ((List<String>) concreteSolutionMapping.getOrDefault("technology", Collections.EMPTY_LIST))) {
//            Aggregator aggregator;
//            String name;
//            String mime;
//
//            switch (technology) {
//                case "ActiveMQ-XML":
//                    aggregator = new ActiveMQXMLAggregator();
//                    name = "camel.xml";
//                    mime = "text/xml";
//                    break;
//                case "ActiveMQ-Java":
//                    aggregator = new ActiveMQJavaAggregator();
//                    name = "PatternAtlasRouteBuilder.java";
//                    mime = "text/x-java";
//                    break;
//                case "AWS-CloudFormation-Json":
//                    aggregator = new AWSCloudFormationJsonAggregator();
//                    name = "CloudFormation-Template.json";
//                    mime = "application/json";
//                    break;
//                default:
//                    log.error("No aggregator for " + technology);
//                    continue;
//            }
//
//
//            String aggregation = aggregator.aggregate(patternInstances, edges, query);
//            aggregatedFiles.add(new FileDTO(name, mime, aggregation));
//        }

        return aggregatedFiles;
    }
}
