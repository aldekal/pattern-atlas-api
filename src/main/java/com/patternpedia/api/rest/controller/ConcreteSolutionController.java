package com.patternpedia.api.rest.controller;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patternpedia.api.entities.designmodel.ConcreteSolution;
import com.patternpedia.api.service.ConcreteSolutionService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.List;
import java.util.UUID;


@RestController
@CommonsLog
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "/concrete-solutions", produces = "application/hal+json")
public class ConcreteSolutionController {

    private ConcreteSolutionService concreteSolutionService;
    private ObjectCodec objectMapper;


    public ConcreteSolutionController(ConcreteSolutionService concreteSolutionService, ObjectMapper objectMapper) {
        this.concreteSolutionService = concreteSolutionService;
        this.objectMapper = objectMapper;
    }


    @GetMapping("")
    public CollectionModel<EntityModel<ConcreteSolution>> getConcreteSolutions(@RequestParam(value = "pattern-uri", required = false) URI patternUri) {
        List<ConcreteSolution> concreteSolutions;

        if (patternUri != null) {
            concreteSolutions = this.concreteSolutionService.getConcreteSolutions(patternUri);
        } else {
            concreteSolutions = this.concreteSolutionService.getConcreteSolutions();
        }

        return CollectionModel.wrap(concreteSolutions);
    }


    @PostMapping("/aggregate/{designModelId}")
    public String aggregateConcreteSolutions(@PathVariable UUID designModelId) {
        return designModelId.toString();
    }
}
