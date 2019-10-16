package com.patternpedia.api.controller;

import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.UndirectedEdge;
import com.patternpedia.api.repositories.DirectedEdgeRepository;
import com.patternpedia.api.repositories.PatternLanguageRepository;
import com.patternpedia.api.repositories.UndirectedEdgeReository;
import com.patternpedia.api.repositories.UndirectedHyperedgeRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/patternLanguages", produces = "application/hal+json")
public class PatternRelationDescriptorController {

    private PatternLanguageRepository patternLanguageRepository;
    private DirectedEdgeRepository directedEdgeRepository;
    private UndirectedEdgeReository undirectedEdgeReository;

    public PatternRelationDescriptorController(PatternLanguageRepository patternLanguageRepository,
                                               DirectedEdgeRepository directedEdgeRepository,
                                               UndirectedEdgeReository undirectedEdgeReository) {
        this.patternLanguageRepository = patternLanguageRepository;
        this.directedEdgeRepository = directedEdgeRepository;
        this.undirectedEdgeReository = undirectedEdgeReository;
    }

    @PostMapping(value = "/{patternLanguageId}/directedEdges")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> createDirectedEdge(@PathVariable UUID patternLanguageId, @RequestBody DirectedEdge directedEdge) {
        PatternLanguage patternLanguage = this.patternLanguageRepository.findById(patternLanguageId)
                .orElseThrow(() -> new ResourceNotFoundException("Pattern Language not found: " + patternLanguageId));

        directedEdge.setPatternLanguage(patternLanguage);
        this.directedEdgeRepository.save(directedEdge);

        if (patternLanguage.getDirectedEdges() != null) {
            patternLanguage.getDirectedEdges().add(directedEdge);
        } else {
            List<DirectedEdge> directedEdges = new ArrayList<>();
            directedEdges.add(directedEdge);
            patternLanguage.setDirectedEdges(directedEdges);
        }
        this.patternLanguageRepository.save(patternLanguage);

        return ResponseEntity
                .created(linkTo(methodOn(PatternLanguageController.class).getDirectedEdgeById(patternLanguageId, directedEdge.getId())).toUri())
                .body(directedEdge);
    }

    @PostMapping(value = "/{patternLanguageId}/undirectedEdges")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> createUndirectedEdge(@PathVariable UUID patternLanguageId, @RequestBody UndirectedEdge undirectedEdge) {
        PatternLanguage patternLanguage = this.patternLanguageRepository.findById(patternLanguageId)
                .orElseThrow(() -> new ResourceNotFoundException("Pattern Language not found: " + patternLanguageId));

        undirectedEdge.setPatternLanguage(patternLanguage);
        this.undirectedEdgeReository.save(undirectedEdge);

        if (patternLanguage.getUndirectedEdges() != null) {
            patternLanguage.getUndirectedEdges().add(undirectedEdge);
        } else {
            List<UndirectedEdge> undirectedEdges = new ArrayList<>();
            undirectedEdges.add(undirectedEdge);
            patternLanguage.setUndirectedEdges(undirectedEdges);
        }
        this.patternLanguageRepository.save(patternLanguage);

        return ResponseEntity
                .created(linkTo(methodOn(PatternLanguageController.class).getUndirectedEdgeById(patternLanguageId, undirectedEdge.getId())).toUri())
                .body(undirectedEdge);
    }

}
