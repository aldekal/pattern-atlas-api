package com.patternpedia.api.rest.controller;

import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.UndirectedEdge;
import com.patternpedia.api.service.PatternRelationDescriptorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(produces = "application/hal+json")
public class PatternRelationDescriptorController {

    private PatternRelationDescriptorService patternRelationDescriptorService;

    public PatternRelationDescriptorController(PatternRelationDescriptorService patternRelationDescriptorService) {
        this.patternRelationDescriptorService = patternRelationDescriptorService;
    }

    @PostMapping(value = "/patternLanguages/{patternLanguageId}/directedEdges")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> createDirectedEdge(@PathVariable UUID patternLanguageId, @RequestBody DirectedEdge directedEdge) {
        directedEdge = this.patternRelationDescriptorService.createDirectedEdge(directedEdge, patternLanguageId);

        return ResponseEntity
                .created(linkTo(methodOn(PatternLanguageController.class).getDirectedEdgeOfPatternLanguageById(patternLanguageId, directedEdge.getId())).toUri())
                .body(directedEdge);
    }

    @PostMapping(value = "/patternLanguages/{patternLanguageId}/undirectedEdges")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> createUndirectedEdge(@PathVariable UUID patternLanguageId, @RequestBody UndirectedEdge undirectedEdge) {
        undirectedEdge = this.patternRelationDescriptorService.createUndirectedEdge(undirectedEdge, patternLanguageId);

        return ResponseEntity
                .created(linkTo(methodOn(PatternLanguageController.class).getUndirectedEdgeOfPatternLanguageById(patternLanguageId, undirectedEdge.getId())).toUri())
                .body(undirectedEdge);
    }

}
