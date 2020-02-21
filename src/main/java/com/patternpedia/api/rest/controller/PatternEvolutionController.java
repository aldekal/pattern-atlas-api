package com.patternpedia.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patternpedia.api.entities.*;
import com.patternpedia.api.exception.DirectedEdgeNotFoundException;
import com.patternpedia.api.exception.UndirectedEdgeNotFoundException;
import com.patternpedia.api.repositories.PatternEvolutionRepository;
import com.patternpedia.api.rest.model.PatternContentModel;
import com.patternpedia.api.rest.model.PatternEvolutionModel;
import com.patternpedia.api.rest.model.PatternLanguageModel;
import com.patternpedia.api.rest.model.PatternModel;
import com.patternpedia.api.service.*;
import org.apache.commons.text.CaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "/patternEvolution", produces = "application/hal+json")
public class PatternEvolutionController {

    Logger logger = LoggerFactory.getLogger(PatternEvolutionController.class);

    private PatternEvolutionService patternEvolutionService;
    private PatternLanguageService patternLanguageService;
    //    private PatternViewService patternViewService;
//    private PatternRelationDescriptorService patternRelationDescriptorService;
    private ObjectMapper objectMapper;

    public PatternEvolutionController(
            PatternEvolutionService patternEvolutionService,
            PatternLanguageService patternLanguageService,
//            PatternViewService patternViewService,
//            PatternRelationDescriptorService patternRelationDescriptorService,
            ObjectMapper objectMapper
    ) {
        this.patternEvolutionService = patternEvolutionService;
        this.patternLanguageService = patternLanguageService;
//        this.patternViewService = patternViewService;
//        this.patternRelationDescriptorService = patternRelationDescriptorService;
        this.objectMapper = objectMapper;
    }

    /**
     * GET Methods
     */
    @GetMapping(value = "/getAll")
    List<PatternEvolution> all() {
        return this.patternEvolutionService.getAllPatternEvolutions();
    }

    @GetMapping(value = "/getById/{patternEvolutionId}")
    PatternEvolution getPatternEvolutionById(@PathVariable UUID patternEvolutionId) {
        return this.patternEvolutionService.getPatternEvolutionById(patternEvolutionId);
    }

    @GetMapping(value = "/getByUri/{patternEvolutionUri}")
    PatternEvolution getPatternEvolutionById(@PathVariable String patternEvolutionUri) {
        return this.patternEvolutionService.getPatternEvolutionByUri(patternEvolutionUri);
    }

    /**
     * CREATE Methods
     */
    @PostMapping(value = "/create")
//    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    PatternEvolution newPatternEvolution(@RequestBody PatternEvolution patternEvolution) {
        logger.info(patternEvolution.toString());
        return this.patternEvolutionService.createPatternEvolution(patternEvolution);
    }

    /**
     * UPDATE Methods
     */
    @PutMapping(value = "/update/{patternEvolutionId}")
    PatternEvolution putPatternLanguage(@PathVariable UUID patternEvolutionId, @RequestBody PatternEvolution patternEvolution) {
        patternEvolution.setId(patternEvolutionId);
        logger.info(patternEvolution.toString());
        return this.patternEvolutionService.updatePatternEvolution(patternEvolution);
    }

    /**
     * DELETE Methods
     */
    @DeleteMapping(value = "/delete/{patternEvolutionId}")
    ResponseEntity<?> deletePatternLanguage(@PathVariable UUID patternEvolutionId) {
        this.patternEvolutionService.deletePatternEvolution(patternEvolutionId);
        return ResponseEntity.noContent().build();
    }
}
