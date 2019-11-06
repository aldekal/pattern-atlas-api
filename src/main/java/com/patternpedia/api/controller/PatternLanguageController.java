package com.patternpedia.api.controller;

import com.patternpedia.api.entities.*;
import com.patternpedia.api.service.PatternLanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "/patternLanguages", produces = "application/hal+json")
public class PatternLanguageController {

    private PatternLanguageService patternLanguageService;

    @Autowired
    public PatternLanguageController(PatternLanguageService patternLanguageService) {
        this.patternLanguageService = patternLanguageService;
    }

    @GetMapping
    Resources<Resource<PatternLanguage>> getAllPatternLanguages() {
        List<Resource<PatternLanguage>> patternLanguages = this.patternLanguageService.getAllPatternLanguages()
                .stream()
                .map(patternLanguage -> new Resource<>(patternLanguage,
                        linkTo(methodOn(PatternLanguageController.class).getAllPatternsOfPatternLanguage(patternLanguage.getId())).withRel("patterns"),
                        linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguage.getId())).withSelfRel(),
                        linkTo(methodOn(PatternLanguageController.class).getAllPatternLanguages()).withRel("patternLanguages")))
                .collect(Collectors.toList());
        return new Resources<>(patternLanguages,
                linkTo(methodOn(PatternLanguageController.class).getAllPatternLanguages()).withSelfRel());
    }

    @GetMapping(value = "/search/findByUri", params = "encodedUri")
    Resource<PatternLanguage> getAllPatternLanguages(@RequestParam String encodedUri) throws UnsupportedEncodingException {
        String uri = URLDecoder.decode(encodedUri, StandardCharsets.UTF_8.toString());

        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageByUri(uri);

        return new Resource<>(patternLanguage,
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguage.getId())).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).getAllPatternsOfPatternLanguage(patternLanguage.getId())).withRel("patterns"),
                linkTo(methodOn(PatternLanguageController.class).getAllPatternLanguages()).withRel("patternLanguages"));
    }

    @GetMapping(value = "/{patternLanguageId}")
    Resource<PatternLanguage> getPatternLanguageById(@PathVariable UUID patternLanguageId) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);
        return new Resource<>(patternLanguage,
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguage.getId())).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).getAllPatternsOfPatternLanguage(patternLanguage.getId())).withRel("patterns"),
                linkTo(methodOn(PatternLanguageController.class).getAllPatternLanguages()).withRel("patternLanguages"));
    }

    @CrossOrigin(exposedHeaders = "Location")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<PatternLanguage> createPatternLanguage(@RequestBody PatternLanguage patternLanguage) {
        PatternLanguage createdPatternLanguage = this.patternLanguageService.createPatternLanguage(patternLanguage);
        return ResponseEntity
                .created(linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(createdPatternLanguage.getId())).toUri())
                .body(createdPatternLanguage);
    }

    @PutMapping(value = "/{patternLanguageId}")
    ResponseEntity<PatternLanguage> updatePatternLanguage(@PathVariable UUID patternLanguageId, @RequestBody PatternLanguage patternLanguage) {
        return ResponseEntity.ok(this.patternLanguageService.updatePatternLanguage(patternLanguage));
    }

    @PostMapping(value = "/{patternLanguageId/patternSchema")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<PatternSchema> createPatternSchema(@PathVariable UUID patternLanguageId, @RequestBody PatternSchema patternSchema) {
        // Todo Implement Integration Test for createPatternSchema
        PatternSchema createdSchema = this.patternLanguageService.createPatternSchemaAndAddToPatternLanguage(patternLanguageId, patternSchema);

        return ResponseEntity
                .created(linkTo(methodOn(PatternLanguageController.class).getPatternSchema(patternLanguageId)).toUri())
                .body(createdSchema);
    }

    // Todo Implement Test for updatePatternSchema
    @PutMapping(value = "/{patternLanguageId}/patternSchema")
    ResponseEntity<PatternSchema> updatePatternSchema(@PathVariable UUID patternLanguageId, @RequestBody PatternSchema patternSchema) {
        PatternSchema schema = this.patternLanguageService.updatePatternSchemaByPatternLanguageId(patternLanguageId, patternSchema);
        return ResponseEntity.ok(schema);
    }

    // Todo Implement Test for getPatternSchema
    @GetMapping(value = "/{patternLanguageId/patternSchema}")
    Resource<PatternSchema> getPatternSchema(@PathVariable UUID patternLanguageId) {
        PatternSchema schema = this.patternLanguageService.getPatternSchemaByPatternLanguageId(patternLanguageId);

        return new Resource<>(schema,
                linkTo(methodOn(PatternLanguageController.class).getPatternSchema(patternLanguageId)).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
    }

    // Todo Implement Integration Test for getAllPatternsOfPatternLanguage
    @GetMapping(value = "/{patternLanguageId}/patterns")
    Resources<Resource<Pattern>> getAllPatternsOfPatternLanguage(@PathVariable UUID patternLanguageId) {
        List<Resource<Pattern>> patterns = this.patternLanguageService.getAllPatternsOfPatternLanguage(patternLanguageId)
                .stream()
                .map(pattern -> new Resource<>(pattern,
                        linkTo(methodOn(PatternLanguageController.class).getPatternOfPatternLanguageById(pattern.getPatternLanguage().getId(), pattern.getId())).withSelfRel(),
                        linkTo(methodOn(PatternLanguageController.class).getPatternContentOfPattern(patternLanguageId, pattern.getId())).withRel("content"),
                        linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(pattern.getPatternLanguage().getId())).withRel("patternLanguage")))
                .collect(Collectors.toList());
        return new Resources<>(patterns,
                linkTo(methodOn(PatternLanguageController.class).getAllPatternsOfPatternLanguage(patternLanguageId)).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));

    }

    @PostMapping(value = "/{patternLanguageId}/patterns")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> addPatternToPatternLanguage(@PathVariable UUID patternLanguageId, @RequestBody Pattern pattern) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);

        pattern.setPatternLanguage(patternLanguage);
        patternLanguage.getPatterns().add(pattern);
        this.patternLanguageService.updatePatternLanguage(patternLanguage);
        return ResponseEntity
                .created(linkTo(methodOn(PatternLanguageController.class).getPatternOfPatternLanguageById(patternLanguageId, pattern.getId())).toUri())
                .body(pattern);
    }

    // Todo Implement Integration Test for deletePatternOfPatternLanguage
    @DeleteMapping(value = "/{patternLanguageId}/patterns/{patternId}")
    ResponseEntity<?> deletePatternOfPatternLanguage(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId) {
        this.patternLanguageService.deletePatternOfPatternLanguage(patternLanguageId, patternId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // Todo Implement Integration Test for getPatternOfPatternLanguageById
    @GetMapping(value = "/{patternLanguageId}/patterns/{patternId}")
    Resource<Pattern> getPatternOfPatternLanguageById(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId) {

        Pattern pattern = this.patternLanguageService.getPatternOfPatternLanguageById(patternLanguageId, patternId);

        return new Resource<>(pattern,
                linkTo(methodOn(PatternLanguageController.class).getPatternOfPatternLanguageById(patternLanguageId, patternId)).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).getPatternContentOfPattern(patternLanguageId, patternId)).withRel("content"),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));

    }

    @GetMapping(value = "/{patternLanguageId}/patterns/{patternId}/content")
    Resource<Object> getPatternContentOfPattern(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId) {

        Pattern pattern = this.patternLanguageService.getPatternOfPatternLanguageById(patternLanguageId, patternId);
        return new Resource<>(pattern.getContent(),
                linkTo(methodOn(PatternLanguageController.class).getPatternContentOfPattern(patternLanguageId, patternId)).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).getPatternOfPatternLanguageById(patternLanguageId, patternId)).withRel("pattern"),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
    }

    @GetMapping(value = "/{patternLanguageId}/undirectedEdges/{undirectedEdgeId}")
    Resource<UndirectedEdge> getUndirectedEdgeOfPatternLanguageById(@PathVariable UUID patternLanguageId, @PathVariable UUID undirectedEdgeId) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);

        UndirectedEdge result = patternLanguage.getUndirectedEdges().stream()
                .filter(undirectedEdge -> undirectedEdge.getId().equals(undirectedEdgeId)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format("UndirectedEdge %s not contained in PatternLanguage %s", undirectedEdgeId, patternLanguageId)));
        return new Resource<>(result,
                linkTo(methodOn(PatternLanguageController.class).getUndirectedEdgeOfPatternLanguageById(patternLanguageId, undirectedEdgeId)).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
    }

    @GetMapping(value = "/{patternLanguageId}/directedEdges/{directedEdgeId}")
    Resource<DirectedEdge> getDirectedEdgeOfPatternLanguageById(@PathVariable UUID patternLanguageId, @PathVariable UUID directedEdgeId) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);

        DirectedEdge result = patternLanguage.getDirectedEdges().stream()
                .filter(directedEdge -> directedEdge.getId().equals(directedEdgeId)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format("DirectedEdge %s not contained in PatternLanguage %s", directedEdgeId, patternLanguageId)));
        return new Resource<>(result,
                linkTo(methodOn(PatternLanguageController.class).getDirectedEdgeOfPatternLanguageById(patternLanguageId, directedEdgeId)).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
    }
}
