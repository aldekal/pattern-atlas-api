package com.patternpedia.api.rest.controller;

import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.PatternSchema;
import com.patternpedia.api.exception.PatternLanguageNotFoundException;
import com.patternpedia.api.service.PatternLanguageService;
import org.apache.commons.text.CaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

        // Todo: This is a hack. How can we influence serialization to prevent embedding content of patterns (--> master assembler)
        List<PatternLanguage> preparedList = this.patternLanguageService.getPatternLanguages();
        for (PatternLanguage patternLanguage : preparedList) {
            if (null != patternLanguage.getPatterns()) {
                patternLanguage.setPatterns(PatternController.removeContentFromPatterns(patternLanguage.getPatterns()));
            }
        }

        List<EntityModel<PatternLanguage>> patternLanguages = preparedList
                .stream()
                .map(patternLanguage -> new EntityModel<>(patternLanguage,
                        getPatternLanguageLinks(patternLanguage.getId())))
                .collect(Collectors.toList());

        return new CollectionModel<>(patternLanguages, getPatternLanguageCollectionLinks());
    }

    @GetMapping(value = "/findByUri")
    EntityModel<PatternLanguage> findPatternLanguageByUri(@RequestParam String encodedUri) throws UnsupportedEncodingException {
        String uri = URLDecoder.decode(encodedUri, StandardCharsets.UTF_8.toString());

        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageByUri(uri);
        if (null != patternLanguage.getPatterns()) {
            patternLanguage.setPatterns(PatternController.removeContentFromPatterns(patternLanguage.getPatterns()));
        }

        return new EntityModel<>(patternLanguage, getPatternLanguageLinks(patternLanguage.getId()));
    }

    @GetMapping(value = "/{patternLanguageId}")
    EntityModel<PatternLanguage> getPatternLanguageById(@PathVariable UUID patternLanguageId) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);
        if (null != patternLanguage.getPatterns()) {
            patternLanguage.setPatterns(PatternController.removeContentFromPatterns(patternLanguage.getPatterns()));
        }

        return new EntityModel<>(patternLanguage, getPatternLanguageLinks(patternLanguage.getId()));
    }

    @PostMapping
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<PatternLanguage> createPatternLanguage(@RequestBody PatternLanguage patternLanguage) {
        String patternLanguageNameAsCamelCase = CaseUtils.toCamelCase(patternLanguage.getName(), false);
        String uri = String.format("https://patternpedia.org/patternLanguages/%s", patternLanguageNameAsCamelCase);
        patternLanguage.setUri(uri);

        PatternLanguage createdPatternLanguage = this.patternLanguageService.createPatternLanguage(patternLanguage);
        return ResponseEntity
                .created(linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(createdPatternLanguage.getId())).toUri())
                .build();
    }

    @PutMapping(value = "/{patternLanguageId}")
    ResponseEntity<PatternLanguage> putPatternLanguage(@PathVariable UUID patternLanguageId, @RequestBody PatternLanguage patternLanguage) {
        return ResponseEntity.ok(this.patternLanguageService.updatePatternLanguage(patternLanguage));
    }

    @DeleteMapping(value = "/{patternLanguageId}")
    ResponseEntity<?> deletePatternLanguage(@PathVariable UUID patternLanguageId) {
        try {
            this.patternLanguageService.deletePatternLanguage(patternLanguageId);
            return ResponseEntity.ok().build();
        } catch (PatternLanguageNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PatchMapping(value = "/{patternLanguageId}")
    ResponseEntity<PatternLanguage> patchPatternLanguage(@PathVariable UUID patternLanguageId, @RequestBody PatternLanguage patternLanguage) {
        return this.putPatternLanguage(patternLanguageId, patternLanguage);
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

    @PutMapping(value = "/{patternLanguageId}/patternSchema")
    ResponseEntity<PatternSchema> updatePatternSchema(@PathVariable UUID patternLanguageId, @RequestBody PatternSchema patternSchema) {
        PatternSchema schema = this.patternLanguageService.updatePatternSchemaOfPatternLanguage(patternLanguageId, patternSchema);
        return ResponseEntity.ok(schema);
    }

    private static List<Link> getPatternLanguageCollectionLinks() {
        List<Link> links = new ArrayList<>();

        try {
            Link findByUriLink = linkTo(methodOn(PatternLanguageController.class).findPatternLanguageByUri(null)).withRel("findByUri");
            links.add(findByUriLink);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        links.add(linkTo(methodOn(PatternLanguageController.class).getAllPatternLanguages()).withSelfRel()
                .andAffordance(afford(methodOn(PatternLanguageController.class).createPatternLanguage(null))));
        return links;
    }

    private static List<Link> getPatternLanguageLinks(UUID patternLanguageId) {
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withSelfRel()
                .andAffordance(afford(methodOn(PatternLanguageController.class).putPatternLanguage(patternLanguageId, null)))
                .andAffordance(afford(methodOn(PatternLanguageController.class).deletePatternLanguage(patternLanguageId)))
        );
        links.add(linkTo(methodOn(PatternController.class).getAllPatternsOfPatternLanguage(patternLanguageId)).withRel("patterns"));
        links.add(linkTo(methodOn(PatternLanguageController.class).getAllPatternLanguages()).withRel("patternLanguages"));
        links.add(linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgesOfPatternLanguage(patternLanguageId)).withRel("directedEdges"));
        links.add(linkTo(methodOn(PatternRelationDescriptorController.class).getUndirectedEdgesOfPatternLanguage(patternLanguageId)).withRel("undirectedEdges"));
        return links;
    }
}
