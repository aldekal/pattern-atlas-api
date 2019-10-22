package com.patternpedia.api.controller;

import com.patternpedia.api.entities.*;
import com.patternpedia.api.repositories.PatternLanguageRepository;
import com.patternpedia.api.repositories.PatternSchemaRepository;
import com.patternpedia.api.repositories.PatternSectionTypeRepository;
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


    // Todo Add Validators for pattern creation, which checks if pattern is compliant to PatternSchema

    private PatternLanguageRepository patternLanguageRepository;
    private PatternSchemaRepository patternSchemaRepository;
    private PatternSectionTypeRepository patternSectionTypeRepository;

    @Autowired
    public PatternLanguageController(PatternLanguageRepository patternLanguageRepository,
                                     PatternSchemaRepository patternSchemaRepository,
                                     PatternSectionTypeRepository patternSectionTypeRepository) {
        this.patternLanguageRepository = patternLanguageRepository;
        this.patternSchemaRepository = patternSchemaRepository;
        this.patternSectionTypeRepository = patternSectionTypeRepository;
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

    @PostMapping
    ResponseEntity<PatternLanguage> create(@RequestBody PatternLanguage patternLanguage) {
        this.patternLanguageRepository.save(patternLanguage);
        return ResponseEntity
                .created(linkTo(methodOn(PatternLanguageController.class).one(patternLanguage.getId())).toUri())
                .body(patternLanguage);
    }

    @PostMapping(value = "/{patternLanguageId/patternSchema")
    ResponseEntity<PatternSchema> createPatternSchema(@PathVariable UUID patternLanguageId, @RequestBody PatternSchema patternSchema) {
        // Todo Implement Test for createPatternSchema
        PatternLanguage patternLanguage = this.patternLanguageRepository.findById(patternLanguageId)
                .orElseThrow(() -> new ResourceNotFoundException("PatternLanguage not found: " + patternLanguageId));
        patternSchema.setPatternLanguage(patternLanguage);

        PatternSchema managedSchema = this.patternSchemaRepository.save(patternSchema);

        return ResponseEntity
                .created(linkTo(methodOn(PatternLanguageController.class).getPatternSchema(patternLanguageId)).toUri())
                .body(managedSchema);
    }

    @PutMapping(value = "/{patternLanguageId}/patternSchema")
    ResponseEntity<?> updatePatternSchema(@PathVariable UUID patternLanguageId, @RequestBody PatternSchema patternSchema) {
        // Todo Implement Test for updatePatternSchema
        PatternLanguage patternLanguage = this.patternLanguageRepository.findById(patternLanguageId)
                .orElseThrow(() -> new ResourceNotFoundException("PatternLanguage not found: " + patternLanguageId));

        if (patternLanguage.getPatternSchema().getId().equals(patternSchema.getId())) {
            PatternSchema managedEntity = this.patternSchemaRepository.save(patternSchema);
            return ResponseEntity.ok(managedEntity);
        } else {
            // PatternLanguage must not be changed
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{patternLanguageId/patternSchema}")
    Resource<PatternSchema> getPatternSchema(@PathVariable UUID patternLanguageId) {
        // Todo Implement Test for getPatternSchema
        PatternLanguage patternLanguage = this.patternLanguageRepository.findById(patternLanguageId)
                .orElseThrow(() -> new ResourceNotFoundException("PatternLanguage not found: " + patternLanguageId));

        return new Resource<>(patternLanguage.getPatternSchema(),
                linkTo(methodOn(PatternLanguageController.class).getPatternSchema(patternLanguage.getId())).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).one(patternLanguage.getId())).withRel("patternLanguage"));
    }

    @PutMapping(value = "/{patternLanguageId}")
    ResponseEntity<?> update(@PathVariable UUID patternLanguageId, @RequestBody PatternLanguage patternLanguage) {
        if (this.patternLanguageRepository.existsById(patternLanguageId)) {
            // Since we found the pattern language we can update it
            this.patternLanguageRepository.save(patternLanguage);
            return ResponseEntity
                    .ok(patternLanguage);
        } else {
            // Since we generate UUIDs in the database, resource generation via PUT with a given UUID is not allowed
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{patternLanguageId}")
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
