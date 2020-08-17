package com.patternpedia.api.rest.controller;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patternpedia.api.entities.designmodel.ConcreteSolution;
import com.patternpedia.api.entities.designmodel.DesignModel;
import com.patternpedia.api.entities.designmodel.DesignModelPatternEdge;
import com.patternpedia.api.entities.designmodel.DesignModelPatternInstance;
import com.patternpedia.api.rest.model.FileDTO;
import com.patternpedia.api.service.ConcreteSolutionService;
import com.patternpedia.api.service.DesignModelService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@CommonsLog
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "/concrete-solutions", produces = "application/hal+json")
public class ConcreteSolutionController {

    private ConcreteSolutionService concreteSolutionService;
    private DesignModelService designModelService;
    private ObjectCodec objectMapper;


    public ConcreteSolutionController(ConcreteSolutionService concreteSolutionService, DesignModelService designModelService, ObjectMapper objectMapper) {
        this.concreteSolutionService = concreteSolutionService;
        this.designModelService = designModelService;
        this.objectMapper = objectMapper;
    }


//    @GetMapping("")
//    public CollectionModel<EntityModel<ConcreteSolution>> getConcreteSolutions(@RequestParam(value = "pattern-uri", required = false) URI patternUri) {
//        List<ConcreteSolution> concreteSolutions;
//
//        if (patternUri != null) {
//            concreteSolutions = this.concreteSolutionService.getConcreteSolutions(patternUri);
//        } else {
//            concreteSolutions = this.concreteSolutionService.getConcreteSolutions();
//        }
//
//        return CollectionModel.wrap(concreteSolutions);
//    }


    @GetMapping("/{designModelId}/concrete-solutions")
    public Set<ConcreteSolution> checkConcreteSolutions(@PathVariable UUID designModelId) {
        List<DesignModelPatternInstance> patternInstanceList = this.designModelService.getDesignModel(designModelId).getPatterns();
        Set<String> patternUris = patternInstanceList.stream().map(patternInstance -> patternInstance.getPattern().getUri()).collect(Collectors.toSet());
        Set<ConcreteSolution> concreteSolutionSet = new HashSet<>();

        for (String uri : patternUris) {
            this.concreteSolutionService.getConcreteSolutions(URI.create(uri)).forEach(concreteSolution -> concreteSolutionSet.add(concreteSolution));
        }

        return concreteSolutionSet;
    }


    @PostMapping("/aggregate/{designModelId}")
    public List<FileDTO> aggregateConcreteSolutions(@PathVariable UUID designModelId, @RequestBody Map<UUID, UUID> query) {

        DesignModel designModel = this.designModelService.getDesignModel(designModelId);
        List<DesignModelPatternInstance> patternInstanceList = designModel.getPatterns();
        List<DesignModelPatternEdge> directedEdgeList = designModel.getDirectedEdges();

        return this.concreteSolutionService.aggregate(patternInstanceList, directedEdgeList, query);
    }
}
