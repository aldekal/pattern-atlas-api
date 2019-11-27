package com.patternpedia.api.rest.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.PatternSchema;
import com.patternpedia.api.service.PatternLanguageService;

import org.apache.commons.text.CaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "/patternLanguages", produces = "application/hal+json")
public class PatternLanguageController {

    private PatternLanguageService patternLanguageService;

    @Autowired
    public PatternLanguageController(PatternLanguageService patternLanguageService) {
        this.patternLanguageService = patternLanguageService;
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
        links.add(linkTo(methodOn(PatternController.class).getPatternsOfPatternLanguage(patternLanguageId)).withRel("patterns"));
        links.add(linkTo(methodOn(PatternLanguageController.class).getAllPatternLanguages()).withRel("patternLanguages"));
        links.add(linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgesOfPatternLanguage(patternLanguageId)).withRel("directedEdges"));
        links.add(linkTo(methodOn(PatternRelationDescriptorController.class).getUndirectedEdgesOfPatternLanguage(patternLanguageId)).withRel("undirectedEdges"));
        return links;
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

        return new EntityModel<>(patternLanguage, getPatternLanguageLinks(patternLanguage.getId()));
    }

    @GetMapping(value = "/{patternLanguageId}")
    EntityModel<PatternLanguage> getPatternLanguageById(@PathVariable UUID patternLanguageId) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);

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
        this.patternLanguageService.deletePatternLanguage(patternLanguageId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{patternLanguageId}/patternSchema")
    EntityModel<PatternSchema> getPatternSchema(@PathVariable UUID patternLanguageId) {

        PatternSchema schema = this.patternLanguageService.getPatternSchemaByPatternLanguageId(patternLanguageId);

        Link selfLink = linkTo(methodOn(PatternLanguageController.class).getPatternSchema(patternLanguageId)).withSelfRel();
        selfLink.andAffordance(afford(methodOn(PatternLanguageController.class).updatePatternSchema(patternLanguageId, null)));

        return new EntityModel<>(schema,
                selfLink,
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
    }

    @PutMapping(value = "/{patternLanguageId}/patternSchema")
    ResponseEntity<PatternSchema> updatePatternSchema(@PathVariable UUID patternLanguageId, @RequestBody PatternSchema patternSchema) {
        PatternSchema schema = this.patternLanguageService.updatePatternSchemaOfPatternLanguage(patternLanguageId, patternSchema);
        return ResponseEntity.ok(schema);
    }
}
