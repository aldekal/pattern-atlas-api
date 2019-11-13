package com.patternpedia.api.rest.controller;

import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.PatternSchema;
import com.patternpedia.api.rest.assembler.PatternLanguageModelAssembler;
import com.patternpedia.api.service.PatternLanguageService;
import org.apache.commons.text.CaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

        // Todo: This is a hack. How can we influence serialization to prevent embedding content of patterns
        List<PatternLanguage> preparedList = this.patternLanguageService.getAllPatternLanguages();
        for (PatternLanguage patternLanguage : preparedList) {
            if (null != patternLanguage.getPatterns()) {
                patternLanguage.setPatterns(PatternController.removeContentFromPatterns(patternLanguage.getPatterns()));
            }
        }

        List<EntityModel<PatternLanguage>> patternLanguages = preparedList
                .stream()
                .map(patternLanguage -> new EntityModel<>(patternLanguage,
                        linkTo(methodOn(PatternController.class).getAllPatternsOfPatternLanguage(patternLanguage.getId())).withRel("patterns"),
                        linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguage.getId())).withSelfRel(),
                        linkTo(methodOn(PatternLanguageController.class).getAllPatternLanguages()).withRel("patternLanguages"),
                        linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgesOfPatternLanguage(patternLanguage.getId())).withRel("directedEdges"),
                        linkTo(methodOn(PatternRelationDescriptorController.class).getUndirectedEdgesOfPatternLanguage(patternLanguage.getId())).withRel("undirectedEdges")))
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
        if (null != patternLanguage.getPatterns()) {
            patternLanguage.setPatterns(PatternController.removeContentFromPatterns(patternLanguage.getPatterns()));
        }

        return new EntityModel<>(patternLanguage,
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguage.getId())).withSelfRel(),
                linkTo(methodOn(PatternController.class).getAllPatternsOfPatternLanguage(patternLanguage.getId())).withRel("patterns"),
                linkTo(methodOn(PatternLanguageController.class).getAllPatternLanguages()).withRel("patternLanguages"),
                linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgesOfPatternLanguage(patternLanguage.getId())).withRel("directedEdges"),
                linkTo(methodOn(PatternRelationDescriptorController.class).getUndirectedEdgesOfPatternLanguage(patternLanguage.getId())).withRel("undirectedEdges"));
    }

    @GetMapping(value = "/{patternLanguageId}")
    EntityModel<PatternLanguage> getPatternLanguageById(@PathVariable UUID patternLanguageId) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);
        if (null != patternLanguage.getPatterns()) {
            patternLanguage.setPatterns(PatternController.removeContentFromPatterns(patternLanguage.getPatterns()));
        }

        return new EntityModel<>(patternLanguage,
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguage.getId())).withSelfRel(),
                linkTo(methodOn(PatternController.class).getAllPatternsOfPatternLanguage(patternLanguage.getId())).withRel("patterns"),
                linkTo(methodOn(PatternLanguageController.class).getAllPatternLanguages()).withRel("patternLanguages"),
                linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgesOfPatternLanguage(patternLanguage.getId())).withRel("directedEdges"),
                linkTo(methodOn(PatternRelationDescriptorController.class).getUndirectedEdgesOfPatternLanguage(patternLanguage.getId())).withRel("undirectedEdges"));
    }

    @PostMapping
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<PatternLanguage> createPatternLanguage(@RequestBody PatternLanguage patternLanguage) {
        // Todo: Generate Uri by camelCased PatternLanguage Name
        String patternLanguageNameAsCamelCase = CaseUtils.toCamelCase(patternLanguage.getName(), false);
        String uri = String.format("https://patternpedia.org/patternLanguages/%s", patternLanguageNameAsCamelCase);
        patternLanguage.setUri(uri);

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

        Link selfLink = linkTo(methodOn(PatternLanguageController.class).getPatternSchema(patternLanguageId)).withSelfRel();
        selfLink.andAffordance(afford(methodOn(PatternLanguageController.class).updatePatternSchema(patternLanguageId, null)));
        selfLink.andAffordance(afford((methodOn(PatternLanguageController.class).createPatternSchema(patternLanguageId, null))));

        return new EntityModel<>(schema,
                selfLink,
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
    }

    @PostMapping(value = "/{patternLanguageId}/patternSchema")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<PatternSchema> createPatternSchema(@PathVariable UUID patternLanguageId, @RequestBody PatternSchema patternSchema) {
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
}
