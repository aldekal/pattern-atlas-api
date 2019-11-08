package com.patternpedia.api.rest.controller;

import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.PatternSchema;
import com.patternpedia.api.entities.UndirectedEdge;
import com.patternpedia.api.service.PatternLanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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
    CollectionModel<EntityModel<PatternLanguage>> getAllPatternLanguages() {
        List<EntityModel<PatternLanguage>> patternLanguages = this.patternLanguageService.getAllPatternLanguages()
                .stream()
                .map(patternLanguage -> new EntityModel<>(patternLanguage,
                        linkTo(methodOn(PatternController.class).getAllPatternsOfPatternLanguage(patternLanguage.getId())).withRel("patterns"),
                        linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguage.getId())).withSelfRel(),
                        linkTo(methodOn(PatternLanguageController.class).getAllPatternLanguages()).withRel("patternLanguages")))
                .collect(Collectors.toList());

        UriTemplate uriTemplate = UriTemplate.of("/findByUri")
                .with(new TemplateVariable("uri", TemplateVariable.VariableType.REQUEST_PARAM));
        Link findByUriLink = null;
        try {
            findByUriLink = linkTo(methodOn(PatternLanguageController.class).findPatternLangaugeByUri(null)).withRel("findByUri");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new CollectionModel<>(patternLanguages,
                findByUriLink,
                linkTo(methodOn(PatternLanguageController.class).getAllPatternLanguages()).withSelfRel());
    }

    @GetMapping(value = "/findByUri")
    EntityModel<PatternLanguage> findPatternLangaugeByUri(@RequestParam String encodedUri) throws UnsupportedEncodingException {
        String uri = URLDecoder.decode(encodedUri, StandardCharsets.UTF_8.toString());

        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageByUri(uri);

        return new EntityModel<>(patternLanguage,
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguage.getId())).withSelfRel(),
                linkTo(methodOn(PatternController.class).getAllPatternsOfPatternLanguage(patternLanguage.getId())).withRel("patterns"),
                linkTo(methodOn(PatternLanguageController.class).getAllPatternLanguages()).withRel("patternLanguages"));
    }

    @GetMapping(value = "/{patternLanguageId}")
    EntityModel<PatternLanguage> getPatternLanguageById(@PathVariable UUID patternLanguageId) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);
        return new EntityModel<>(patternLanguage,
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguage.getId())).withSelfRel(),
                linkTo(methodOn(PatternController.class).getAllPatternsOfPatternLanguage(patternLanguage.getId())).withRel("patterns"),
                linkTo(methodOn(PatternLanguageController.class).getAllPatternLanguages()).withRel("patternLanguages"));
    }

    @PostMapping
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<PatternLanguage> createPatternLanguage(@RequestBody PatternLanguage patternLanguage) {
        PatternLanguage createdPatternLanguage = this.patternLanguageService.createPatternLanguage(patternLanguage);
        return ResponseEntity
                .created(linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(createdPatternLanguage.getId())).toUri())
                .body(createdPatternLanguage);
    }

    @PutMapping(value = "/{patternLanguageId}")
    ResponseEntity<PatternLanguage> putPatternLanguage(@PathVariable UUID patternLanguageId, @RequestBody PatternLanguage patternLanguage) {
        return ResponseEntity.ok(this.patternLanguageService.updatePatternLanguage(patternLanguage));
    }

    @PatchMapping(value = "/{patternLanguageId}")
    ResponseEntity<PatternLanguage> patchPatternLanguage(@PathVariable UUID patternLanguageId, @RequestBody PatternLanguage patternLanguage) {
        return ResponseEntity.ok(this.patternLanguageService.updatePatternLanguage(patternLanguage));
    }

    @GetMapping(value = "/{patternLanguageId}/patternSchema")
    EntityModel<PatternSchema> getPatternSchema(@PathVariable UUID patternLanguageId) {

        PatternSchema schema = this.patternLanguageService.getPatternSchemaByPatternLanguageId(patternLanguageId);

        return new EntityModel<>(schema,
                linkTo(methodOn(PatternLanguageController.class).getPatternSchema(patternLanguageId)).withSelfRel()
                        .andAffordance(afford(methodOn(PatternLanguageController.class).updatePatternSchema(patternLanguageId, null))),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
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

    @GetMapping(value = "/{patternLanguageId}/undirectedEdges/{undirectedEdgeId}")
    EntityModel<UndirectedEdge> getUndirectedEdgeOfPatternLanguageById(@PathVariable UUID patternLanguageId, @PathVariable UUID undirectedEdgeId) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);

        UndirectedEdge result = patternLanguage.getUndirectedEdges().stream()
                .filter(undirectedEdge -> undirectedEdge.getId().equals(undirectedEdgeId)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format("UndirectedEdge %s not contained in PatternLanguage %s", undirectedEdgeId, patternLanguageId)));
        return new EntityModel<>(result,
                linkTo(methodOn(PatternLanguageController.class).getUndirectedEdgeOfPatternLanguageById(patternLanguageId, undirectedEdgeId)).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
    }

    @GetMapping(value = "/{patternLanguageId}/directedEdges/{directedEdgeId}")
    EntityModel<DirectedEdge> getDirectedEdgeOfPatternLanguageById(@PathVariable UUID patternLanguageId, @PathVariable UUID directedEdgeId) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);

        DirectedEdge result = patternLanguage.getDirectedEdges().stream()
                .filter(directedEdge -> directedEdge.getId().equals(directedEdgeId)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format("DirectedEdge %s not contained in PatternLanguage %s", directedEdgeId, patternLanguageId)));
        return new EntityModel<>(result,
                linkTo(methodOn(PatternLanguageController.class).getDirectedEdgeOfPatternLanguageById(patternLanguageId, directedEdgeId)).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
    }
}
