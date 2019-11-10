package com.patternpedia.api.rest.controller;

import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.UndirectedEdge;
import com.patternpedia.api.service.PatternLanguageService;
import com.patternpedia.api.service.PatternRelationDescriptorService;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(produces = "application/hal+json")
public class PatternRelationDescriptorController {

    private PatternRelationDescriptorService patternRelationDescriptorService;
    private PatternLanguageService patternLanguageService;

    public PatternRelationDescriptorController(PatternRelationDescriptorService patternRelationDescriptorService,
                                               PatternLanguageService patternLanguageService) {
        this.patternRelationDescriptorService = patternRelationDescriptorService;
        this.patternLanguageService = patternLanguageService;
    }

    @PostMapping(value = "/patternLanguages/{patternLanguageId}/directedEdges")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> createDirectedEdge(@PathVariable UUID patternLanguageId, @RequestBody DirectedEdge directedEdge) {
        directedEdge = this.patternRelationDescriptorService.createDirectedEdge(directedEdge, patternLanguageId);

        return ResponseEntity
                .created(linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgeOfPatternLanguageById(patternLanguageId, directedEdge.getId())).toUri())
                .body(directedEdge);
    }

    @PostMapping(value = "/patternLanguages/{patternLanguageId}/undirectedEdges")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> createUndirectedEdge(@PathVariable UUID patternLanguageId, @RequestBody UndirectedEdge undirectedEdge) {
        undirectedEdge = this.patternRelationDescriptorService.createUndirectedEdge(undirectedEdge, patternLanguageId);

        return ResponseEntity
                .created(linkTo(methodOn(PatternRelationDescriptorController.class).getUndirectedEdgeOfPatternLanguageById(patternLanguageId, undirectedEdge.getId())).toUri())
                .body(undirectedEdge);
    }

    @GetMapping(value = "/patternLanguages/{patternLanguageId}/undirectedEdges/{undirectedEdgeId}")
    EntityModel<UndirectedEdge> getUndirectedEdgeOfPatternLanguageById(@PathVariable UUID patternLanguageId, @PathVariable UUID undirectedEdgeId) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);

        UndirectedEdge result = patternLanguage.getUndirectedEdges().stream()
                .filter(undirectedEdge -> undirectedEdge.getId().equals(undirectedEdgeId)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format("UndirectedEdge %s not contained in PatternLanguage %s", undirectedEdgeId, patternLanguageId)));
        return new EntityModel<>(result,
                linkTo(methodOn(PatternRelationDescriptorController.class).getUndirectedEdgeOfPatternLanguageById(patternLanguageId, undirectedEdgeId)).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
    }

    @GetMapping(value = "/patternLanguages/{patternLanguageId}/directedEdges/{directedEdgeId}")
    EntityModel<DirectedEdge> getDirectedEdgeOfPatternLanguageById(@PathVariable UUID patternLanguageId, @PathVariable UUID directedEdgeId) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);

        DirectedEdge result = patternLanguage.getDirectedEdges().stream()
                .filter(directedEdge -> directedEdge.getId().equals(directedEdgeId)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format("DirectedEdge %s not contained in PatternLanguage %s", directedEdgeId, patternLanguageId)));
        return new EntityModel<>(result,
                linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgeOfPatternLanguageById(patternLanguageId, directedEdgeId)).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
    }

}
