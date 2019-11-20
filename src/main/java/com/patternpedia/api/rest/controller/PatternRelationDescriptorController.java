package com.patternpedia.api.rest.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.UndirectedEdge;
import com.patternpedia.api.service.PatternLanguageService;
import com.patternpedia.api.service.PatternViewService;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    private PatternViewService patternViewService;

    public PatternRelationDescriptorController(PatternLanguageService patternLanguageService,
                                               PatternViewService patternViewService) {
        this.patternLanguageService = patternLanguageService;
        this.patternViewService = patternViewService;
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
    ResponseEntity<?> createDirectedEdgeAndAddToPatternLanguage(@PathVariable UUID patternLanguageId, @RequestBody DirectedEdge directedEdge) {
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
    ResponseEntity<?> createUndirectedEdgeAndAddToPatternLanguage(@PathVariable UUID patternLanguageId, @RequestBody UndirectedEdge undirectedEdge) {
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

    @PostMapping(value = "/patternViews/{patternViewId}/directedEdges")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> addDirectedEdgeToView(@PathVariable UUID patternViewId, @RequestBody DirectedEdge directedEdge) {
        if (null != directedEdge.getId()) {
            this.patternViewService.addDirectedEdgeToPatternView(patternViewId, directedEdge.getId());
        } else {
            directedEdge = this.patternViewService.createDirectedEdgeAndAddToPatternView(patternViewId, directedEdge);
        }

        return ResponseEntity.created(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getDirectedEdgeOfPatternViewById(patternViewId, directedEdge.getId())).toUri()).build();
    }

    @GetMapping(value = "/patternViews/{patternViewId}/directedEdges/{directedEdgeId}")
    EntityModel<DirectedEdge> getDirectedEdgeOfPatternViewById(@PathVariable UUID patternViewId, @PathVariable UUID directedEdgeId) {
        DirectedEdge directedEdge = this.patternViewService.getDirectedEdgeOfPatternViewById(patternViewId, directedEdgeId);
        return new EntityModel<>(directedEdge,
                linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgeOfPatternViewById(patternViewId, directedEdgeId)).withSelfRel(),
                linkTo(methodOn(PatternViewController.class).getPatternViewById(patternViewId)).withRel("patternView")
        );
        // Todo: Add Affordances to DirectedEdge!
    }

    @DeleteMapping(value = "/patternViews/{patternViewId}/directedEdges/{directedEdgeId}")
    ResponseEntity<?> removeDirectedEdgeFromPatternView(@PathVariable UUID patternViewId, @PathVariable UUID directedEdgeId) {
        this.patternViewService.removeDirectedEdgeFromPatternView(patternViewId, directedEdgeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/patternViews/{patternViewId}/undirectedEdges")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> addUndirectedEdgeToView(@PathVariable UUID patternViewId, @RequestBody UndirectedEdge undirectedEdge) {
        if (null != undirectedEdge.getId()) {
            this.patternViewService.addUndirectedEdgeToPatternView(patternViewId, undirectedEdge.getId());
        } else {
            undirectedEdge = this.patternViewService.createUndirectedEdgeAndAddToPatternView(patternViewId, undirectedEdge);
        }

        return ResponseEntity.created(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getUndirectedEdgeOfPatternViewById(patternViewId, undirectedEdge.getId())).toUri()).build();
    }

    @GetMapping(value = "/patternViews/{patternViewId}/undirectedEdges/{undirectedEdgeId}")
    EntityModel<UndirectedEdge> getUndirectedEdgeOfPatternViewById(@PathVariable UUID patternViewId, @PathVariable UUID undirectedEdgeId) {
        UndirectedEdge undirectedEdge = this.patternViewService.getUndirectedEdgeOfPatternViewById(patternViewId, undirectedEdgeId);
        return new EntityModel<>(undirectedEdge,
                linkTo(methodOn(PatternRelationDescriptorController.class).getUndirectedEdgeOfPatternViewById(patternViewId, undirectedEdgeId)).withSelfRel(),
                linkTo(methodOn(PatternViewController.class).getPatternViewById(patternViewId)).withRel("patternView")
        );
        // Todo: Add Affordances to UndirectedEdge!
    }

    @DeleteMapping(value = "/patternViews/{patternViewId}/undirectedEdges/{undirectedEdgeId}")
    ResponseEntity<?> removeUndirectedEdgeFromPatternView(@PathVariable UUID patternViewId, @PathVariable UUID undirectedEdgeId) {
        this.patternViewService.removeUndirectedEdgeFromPatternView(patternViewId, undirectedEdgeId);
        return ResponseEntity.ok().build();
    }
}
