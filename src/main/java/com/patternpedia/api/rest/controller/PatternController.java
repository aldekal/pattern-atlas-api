package com.patternpedia.api.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.PatternView;
import com.patternpedia.api.service.PatternLanguageService;
import com.patternpedia.api.service.PatternService;
import com.patternpedia.api.service.PatternViewService;

import javax.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(produces = "application/hal+json")
public class PatternController {

    private PatternService patternService;
    private PatternLanguageService patternLanguageService;
    private PatternViewService patternViewService;

    public PatternController(PatternService patternService,
                             PatternLanguageService patternLanguageService,
                             PatternViewService patternViewService) {
        this.patternService = patternService;
        this.patternLanguageService = patternLanguageService;
        this.patternViewService = patternViewService;
    }

    static List<Pattern> removeContentFromPatterns(List<Pattern> patterns) {
        for (Pattern pattern : patterns) {
            pattern.setContent(null);
        }
        return patterns;
    }

    static List<Link> getPatternLinks(Pattern pattern) {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(pattern.getPatternLanguage().getId(), pattern.getId())).withSelfRel()
                .andAffordance(afford(methodOn(PatternController.class).updatePatternViaPut(pattern.getPatternLanguage().getId(), pattern.getId(), null)))
                .andAffordance(afford(methodOn(PatternController.class).updatePatternViaPatch(pattern.getPatternLanguage().getId(), pattern.getId(), null)))
                .andAffordance(afford(methodOn(PatternController.class).deletePatternOfPatternLanguage(pattern.getPatternLanguage().getId(), pattern.getId()))));
        links.add(linkTo(methodOn(PatternController.class).getPatternContentOfPattern(pattern.getPatternLanguage().getId(), pattern.getId())).withRel("content"));
        links.add(linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(pattern.getPatternLanguage().getId())).withRel("patternLanguage"));

        if (null != pattern.getPatternViews()) {
            for (PatternView patternView : pattern.getPatternViews()) {
                links.add(linkTo(methodOn(PatternViewController.class).getPatternViewById(patternView.getId())).withRel("patternView"));
            }
        }

        return links;
    }

    static List<Link> getPatternLanguagePatternCollectionLinks(UUID patternLanguageId) {
        ArrayList<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(PatternController.class).getPatternsOfPatternLanguage(patternLanguageId)).withSelfRel()
                .andAffordance(afford(methodOn(PatternController.class).addPatternToPatternLanguage(patternLanguageId, null))));
        links.add(linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));

        return links;
    }

    static List<Link> getPatternViewPatternCollectionLinks(UUID patternViewId) {
        List<Link> links = new ArrayList<>();
        links.add(
                linkTo(methodOn(PatternController.class).getPatternsOfPatternView(patternViewId)).withSelfRel()
                        .andAffordance(afford(methodOn(PatternController.class).addPatternToPatternView(patternViewId, null)))
        );
        links.add(linkTo(methodOn(PatternViewController.class).getPatternViewById(patternViewId)).withRel("patternView"));
        return links;
    }

    @GetMapping(value = "/patternLanguages/{patternLanguageId}/patterns")
    CollectionModel<EntityModel<Pattern>> getPatternsOfPatternLanguage(@PathVariable UUID patternLanguageId) {
        // Todo: This is a hack. How can we influence serialization to prevent embedding content of patterns (--> master assembler)
        List<Pattern> preparedList = removeContentFromPatterns(this.patternLanguageService.getPatternsOfPatternLanguage(patternLanguageId));

        List<EntityModel<Pattern>> patterns = preparedList
                .stream()
                .map(pattern -> new EntityModel<>(pattern, getPatternLinks(pattern)))
                .collect(Collectors.toList());

        return new CollectionModel<>(patterns, getPatternLanguagePatternCollectionLinks(patternLanguageId));
    }

    @GetMapping(value = "/patternViews/{patternViewId}/patterns")
    CollectionModel<EntityModel<Pattern>> getPatternsOfPatternView(@PathVariable UUID patternViewId) {

        // Todo: This is a hack. How can we influence serialization to prevent embedding content of patterns
        List<Pattern> preparedList = removeContentFromPatterns(this.patternViewService.getPatternsOfPatternView(patternViewId));

        List<EntityModel<Pattern>> patterns = preparedList
                .stream()
                .map(pattern -> new EntityModel<>(pattern, getPatternLinks(pattern)))
                .collect(Collectors.toList());
        return new CollectionModel<>(patterns, getPatternViewPatternCollectionLinks(patternViewId));
    }

    @PostMapping(value = "/patternViews/{patternViewId}/patterns")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addPatternToPatternView(@PathVariable UUID patternViewId, @RequestBody Pattern pattern) {
        this.patternViewService.addPatternToPatternView(patternViewId, pattern.getId());
        return ResponseEntity.created(linkTo(methodOn(PatternController.class)
                .getPatternOfPatternViewById(patternViewId, pattern.getId())).toUri()).build();
    }

    @GetMapping(value = "/patternLanguages/{patternLanguageId}/patterns/{patternId}")
    EntityModel<Pattern> getPatternOfPatternLanguageById(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId) {
        Pattern pattern = this.patternLanguageService.getPatternOfPatternLanguageById(patternLanguageId, patternId);
        return new EntityModel<>(pattern, getPatternLinks(pattern));
    }

    @GetMapping(value = "/patternViews/{patternViewId}/patterns/{patternId}")
    EntityModel<Pattern> getPatternOfPatternViewById(UUID patternViewId, UUID patternId) {
        Pattern pattern = this.patternViewService.getPatternOfPatternViewById(patternViewId, patternId);
        return new EntityModel<>(pattern, getPatternLinks(pattern));
    }

    @PostMapping(value = "/patternLanguages/{patternLanguageId}/patterns")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> addPatternToPatternLanguage(@PathVariable UUID patternLanguageId, @Valid @RequestBody Pattern pattern) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);

        pattern = this.patternLanguageService.createPatternAndAddToPatternLanguage(patternLanguageId, pattern);

        return ResponseEntity
                .created(linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(patternLanguageId, pattern.getId())).toUri())
                .build();
    }

    @PutMapping(value = "/patternLanguages/{patternLanguageId}/patterns/{patternId}")
    EntityModel<Pattern> updatePatternViaPut(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId, @Valid @RequestBody Pattern pattern) {
        pattern = this.patternService.updatePattern(pattern);
        return new EntityModel<>(pattern,
                linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(patternLanguageId, patternId)).withSelfRel(),
                linkTo(methodOn(PatternController.class).getPatternContentOfPattern(patternLanguageId, patternId)).withRel("content"),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
    }

    @PatchMapping(value = "/patternLanguages/{patternLanguageId}/patterns/{patternId}")
    EntityModel<Pattern> updatePatternViaPatch(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId, @Valid @RequestBody Pattern pattern) {
        pattern = this.patternService.updatePattern(pattern);
        return new EntityModel<>(pattern,
                linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(patternLanguageId, patternId)).withSelfRel(),
                linkTo(methodOn(PatternController.class).getPatternContentOfPattern(patternLanguageId, patternId)).withRel("content"),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
    }

    @DeleteMapping(value = "/patternLanguages/{patternLanguageId}/patterns/{patternId}")
    ResponseEntity<?> deletePatternOfPatternLanguage(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId) {
        this.patternLanguageService.deletePatternOfPatternLanguage(patternLanguageId, patternId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/patternLanguages/{patternLanguageId}/patterns/{patternId}/content")
    EntityModel<Object> getPatternContentOfPattern(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId) {

        Pattern pattern = this.patternLanguageService.getPatternOfPatternLanguageById(patternLanguageId, patternId);

        return new EntityModel<>(pattern.getContent(),
                linkTo(methodOn(PatternController.class).getPatternContentOfPattern(patternLanguageId, patternId)).withSelfRel(),
                linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(patternLanguageId, patternId)).withRel("pattern"),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
    }
}
