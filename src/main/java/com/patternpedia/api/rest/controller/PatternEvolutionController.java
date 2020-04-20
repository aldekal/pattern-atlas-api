package com.patternpedia.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patternpedia.api.entities.pattern.evolution.CommentPatternEvolution;
import com.patternpedia.api.entities.pattern.evolution.PatternEvolution;
import com.patternpedia.api.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
//@CrossOrigin(allowedHeaders = "*", origins = "*")
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
    @PreAuthorize(value = "#oauth2.hasScope('read')")
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
//    @PreAuthorize(value = "#oauth2.hasScope('write')")
//    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    PatternEvolution newPatternEvolution(@RequestBody PatternEvolution patternEvolution) {
        return this.patternEvolutionService.createPatternEvolution(patternEvolution);
    }

    @PostMapping(value = "/createComment/{patternEvolutionId}&{userId}")
//    @PreAuthorize(value = "#oauth2.hasScope('write')")
//    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    PatternEvolution newPatternEvolutionComment(@PathVariable UUID patternEvolutionId, @PathVariable UUID userId, @RequestBody CommentPatternEvolution commentPatternEvolution) {
        return this.patternEvolutionService.createComment(patternEvolutionId, userId, commentPatternEvolution);
    }

    /**
     * UPDATE Methods
     */
    @PutMapping(value = "/update/{patternEvolutionId}")
    PatternEvolution putPatternEvolution(@PathVariable UUID patternEvolutionId, @RequestBody PatternEvolution patternEvolution) {
        patternEvolution.setId(patternEvolutionId);
        logger.info(patternEvolution.toString());
        return this.patternEvolutionService.updatePatternEvolution(patternEvolution);
    }

    @PutMapping(value = "/updateRating/{patternEvolutionId}&{userId}&{rating}")
    PatternEvolution putPatternEvolutionRating(@PathVariable UUID patternEvolutionId, @PathVariable UUID userId, @PathVariable String rating) {
        return this.patternEvolutionService.userRating(patternEvolutionId, userId, rating);
    }

    /**
     * DELETE Methods
     */
    @DeleteMapping(value = "/delete/{patternEvolutionId}")
//    @PreAuthorize(value = "#oauth2.hasScope('de')")
    ResponseEntity<?> deletePatternEvolution(@PathVariable UUID patternEvolutionId) {
        this.patternEvolutionService.deletePatternEvolution(patternEvolutionId);
        return ResponseEntity.noContent().build();
    }
}
