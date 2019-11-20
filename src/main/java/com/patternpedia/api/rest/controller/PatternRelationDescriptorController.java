package com.patternpedia.api.rest.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.UndirectedEdge;
import com.patternpedia.api.service.PatternLanguageService;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(produces = "application/hal+json")
public class PatternRelationDescriptorController {

    private PatternLanguageService patternLanguageService;

    public PatternRelationDescriptorController(PatternLanguageService patternLanguageService) {
        this.patternLanguageService = patternLanguageService;
    }

    @GetMapping(value = "/patternLanguages/{patternLanguageId}/directedEdges")
    CollectionModel<EntityModel<DirectedEdge>> getDirectedEdgesOfPatternLanguage(@PathVariable UUID patternLanguageId) {
        List<EntityModel<DirectedEdge>> directedEdges = this.patternLanguageService.getDirectedEdgesOfPatternLanguage(patternLanguageId)
                .stream()
                .map(directedEdge -> new EntityModel<>(directedEdge,
                        linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgeOfPatternLanguageById(patternLanguageId, directedEdge.getId())).withSelfRel(),
                        linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgesOfPatternLanguage(patternLanguageId)).withRel("directedEdges"),
                        linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(patternLanguageId, directedEdge.getSource().getId())).withRel("source"),
                        linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(patternLanguageId, directedEdge.getTarget().getId())).withRel("target"))
                ).collect(Collectors.toList());
        return new CollectionModel<>(directedEdges,
                linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgesOfPatternLanguage(patternLanguageId)).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage")
        );
    }

    @PostMapping(value = "/patternLanguages/{patternLanguageId}/directedEdges")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> createDirectedEdge(@PathVariable UUID patternLanguageId, @RequestBody DirectedEdge directedEdge) {
        directedEdge = this.patternLanguageService.createDirectedEdgeAndAddToPatternLanguage(patternLanguageId, directedEdge);

        return ResponseEntity
                .created(linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgeOfPatternLanguageById(patternLanguageId, directedEdge.getId())).toUri())
                .body(directedEdge);
    }

    @GetMapping(value = "/patternLanguages/{patternLanguageId}/undirectedEdges")
    CollectionModel<EntityModel<UndirectedEdge>> getUndirectedEdgesOfPatternLanguage(@PathVariable UUID patternLanguageId) {
        List<EntityModel<UndirectedEdge>> undirectedEdges = this.patternLanguageService.getUndirectedEdgesOfPatternLanguage(patternLanguageId)
                .stream()
                .map(undirectedEdge -> new EntityModel<>(undirectedEdge,
                        linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgeOfPatternLanguageById(patternLanguageId, undirectedEdge.getId())).withSelfRel(),
                        linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgesOfPatternLanguage(patternLanguageId)).withRel("directedEdges"),
                        linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(patternLanguageId, undirectedEdge.getP1().getId())).withRel("pattern1"),
                        linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(patternLanguageId, undirectedEdge.getP2().getId())).withRel("pattern2"))
                ).collect(Collectors.toList());
        return new CollectionModel<>(undirectedEdges,
                linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgesOfPatternLanguage(patternLanguageId)).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage")
        );
    }

    @PostMapping(value = "/patternLanguages/{patternLanguageId}/undirectedEdges")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> createUndirectedEdge(@PathVariable UUID patternLanguageId, @RequestBody UndirectedEdge undirectedEdge) {
        undirectedEdge = this.patternLanguageService.createUndirectedEdgeAndAddToPatternLanguage(patternLanguageId, undirectedEdge);

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
