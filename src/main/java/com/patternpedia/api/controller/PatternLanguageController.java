package com.patternpedia.api.controller;

import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.UndirectedEdge;
import com.patternpedia.api.repositories.PatternLanguageRepository;
import com.patternpedia.api.repositories.PatternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/patternLanguages", produces = "application/hal+json")
public class PatternLanguageController {

    private PatternLanguageRepository patternLanguageRepository;
    private PatternRepository patternRepository;

    @Autowired
    public PatternLanguageController(PatternLanguageRepository patternLanguageRepository,
                                     PatternRepository patternRepository) {
        this.patternLanguageRepository = patternLanguageRepository;
        this.patternRepository = patternRepository;
    }

    @GetMapping
    Resources<Resource<PatternLanguage>> all() {
        List<Resource<PatternLanguage>> patternLanguages = this.patternLanguageRepository.findAll()
                .stream()
                .map(patternLanguage -> new Resource<>(patternLanguage,
                        linkTo(methodOn(PatternLanguageController.class).one(patternLanguage.getId())).withSelfRel(),
                        linkTo(methodOn(PatternLanguageController.class).all()).withRel("patternLanguages")))
                .collect(Collectors.toList());
        return new Resources<>(patternLanguages,
                linkTo(methodOn(PatternLanguageController.class).all()).withSelfRel());
    }

    @GetMapping(value = "{patternLanguageId}")
    Resource<PatternLanguage> one(@PathVariable UUID patternLanguageId) {
        PatternLanguage patternLanguage = this.patternLanguageRepository.findById(patternLanguageId)
                .orElseThrow(() -> new ResourceNotFoundException(patternLanguageId.toString()));
        return new Resource<>(patternLanguage,
                linkTo(methodOn(PatternLanguageController.class).one(patternLanguage.getId())).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).all()).withRel("patternLanguages"));
    }

    @PostMapping(value = "/{patternLanguageId}/patterns")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> addPattern(@PathVariable UUID patternLanguageId, @RequestBody Pattern pattern) {
        PatternLanguage patternLanguage = this.patternLanguageRepository.findById(patternLanguageId)
                .orElseThrow(() -> new ResourceNotFoundException("PatternLanguage not found: " + patternLanguageId.toString()));
        pattern.setPatternLanguage(patternLanguage);
        patternLanguage.getPatterns().add(pattern);
        this.patternLanguageRepository.save(patternLanguage);
        return ResponseEntity
                .created(linkTo(methodOn(PatternLanguageController.class).getPatternById(patternLanguageId, pattern.getId())).toUri())
                .body(pattern);
    }

    @GetMapping(value = "/{patternLanguageId}/patterns/{patternId}")
    Resource<Pattern> getPatternById(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId) {
        PatternLanguage patternLanguage = this.patternLanguageRepository.findById(patternLanguageId)
                .orElseThrow(() -> new ResourceNotFoundException("Pattern Language not found: " + patternLanguageId));
        Pattern result = patternLanguage.getPatterns().stream()
                .filter(pattern -> pattern.getId().equals(patternId)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Pattern not found: " + patternId));
        return new Resource<>(result,
                linkTo(methodOn(PatternLanguageController.class).getPatternById(patternLanguageId, patternId)).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).one(patternLanguageId)).withRel("patternLanguage"));

    }

    @GetMapping(value = "/{patternLanguageId}/undirectedEdges/{undirectedEdgeId}")
    Resource<UndirectedEdge> getUndirectedEdgeById(@PathVariable UUID patternLanguageId, @PathVariable Long undirectedEdgeId) {
        PatternLanguage patternLanguage = this.patternLanguageRepository.findById(patternLanguageId)
                .orElseThrow(() -> new ResourceNotFoundException("Pattern Language not found: " + patternLanguageId));

        UndirectedEdge result = patternLanguage.getUndirectedEdges().stream()
                .filter(undirectedEdge -> undirectedEdge.getId().equals(undirectedEdgeId)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format("UndirectedEdge %s not contained in PatternLanguage %s", undirectedEdgeId, patternLanguageId)));
        return new Resource<>(result,
                linkTo(methodOn(PatternLanguageController.class).getUndirectedEdgeById(patternLanguageId, undirectedEdgeId)).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).one(patternLanguageId)).withRel("patternLanguage"));
    }

    @GetMapping(value = "/{patternLanguageId}/directedEdges/{directedEdgeId}")
    Resource<DirectedEdge> getDirectedEdgeById(@PathVariable UUID patternLanguageId, @PathVariable Long directedEdgeId) {
        PatternLanguage patternLanguage = this.patternLanguageRepository.findById(patternLanguageId)
                .orElseThrow(() -> new ResourceNotFoundException("Pattern Language not found: " + patternLanguageId));

        DirectedEdge result = patternLanguage.getDirectedEdges().stream()
                .filter(directedEdge -> directedEdge.getId().equals(directedEdgeId)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format("DirectedEdge %s not contained in PatternLanguage %s", directedEdgeId, patternLanguageId)));
        return new Resource<>(result,
                linkTo(methodOn(PatternLanguageController.class).getDirectedEdgeById(patternLanguageId, directedEdgeId)).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).one(patternLanguageId)).withRel("patternLanguage"));
    }
}
