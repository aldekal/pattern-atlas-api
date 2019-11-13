package com.patternpedia.api.rest.controller;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.service.PatternLanguageService;
import com.patternpedia.api.service.PatternService;
import com.patternpedia.api.service.PatternViewService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public static List<Pattern> removeContentFromPatterns(List<Pattern> patterns) {
        for (Pattern pattern : patterns) {
            pattern.setContent(null);
        }
        return patterns;
    }

    @GetMapping(value = "/patternLanguages/{patternLanguageId}/patterns")
    CollectionModel<EntityModel<Pattern>> getAllPatternsOfPatternLanguage(@PathVariable UUID patternLanguageId) {
        // Todo: This is a hack. How can we influence serialization to prevent embedding content of patterns
        List<Pattern> preparedList = removeContentFromPatterns(this.patternLanguageService.getAllPatternsOfPatternLanguage(patternLanguageId));

        List<EntityModel<Pattern>> patterns = preparedList
                .stream()
                .map(pattern -> new EntityModel<>(pattern,
                        linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(pattern.getPatternLanguage().getId(), pattern.getId())).withSelfRel(),
                        linkTo(methodOn(PatternController.class).getPatternContentOfPattern(patternLanguageId, pattern.getId())).withRel("content"),
                        linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(pattern.getPatternLanguage().getId())).withRel("patternLanguage")))
                .collect(Collectors.toList());
        return new CollectionModel<>(patterns,
                linkTo(methodOn(PatternController.class).getAllPatternsOfPatternLanguage(patternLanguageId)).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));

    }

    @GetMapping(value = "/patternViews/{patternViewId}/patterns")
    CollectionModel<EntityModel<Pattern>> getAllPatternsOfPatternView(@PathVariable UUID patternViewId) {

        // Todo: This is a hack. How can we influence serialization to prevent embedding content of patterns
        List<Pattern> preparedList = removeContentFromPatterns(this.patternViewService.getAllPatternsOfPatternView(patternViewId));

        List<EntityModel<Pattern>> patterns = preparedList
                .stream()
                .map(pattern -> new EntityModel<>(pattern,
                        linkTo(methodOn(PatternController.class).getPatternOfPatternViewById(patternViewId, pattern.getId())).withSelfRel(),
                        linkTo(methodOn(PatternController.class).getPatternContentOfPattern(pattern.getPatternLanguage().getId(), pattern.getId())).withRel("content"),
                        linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(pattern.getPatternLanguage().getId())).withRel("patternLanguage")))
                .collect(Collectors.toList());
        return new CollectionModel<>(patterns,
                linkTo(methodOn(PatternController.class).getAllPatternsOfPatternView(patternViewId)).withSelfRel(),
                linkTo(methodOn(PatternViewController.class).getPatternViewById(patternViewId)).withRel("patternView"));

    }

    @GetMapping(value = "/patternViews/{patternViewId}/patterns/{patternId}")
    EntityModel<Pattern> getPatternOfPatternViewById(UUID patternViewId, UUID patternId) {
        throw new UnsupportedOperationException();
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

    @GetMapping(value = "/patternLanguages/{patternLanguageId}/patterns/{patternId}")
    EntityModel<Pattern> getPatternOfPatternLanguageById(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId) {

        Pattern pattern = this.patternLanguageService.getPatternOfPatternLanguageById(patternLanguageId, patternId);

        return new EntityModel<>(pattern,
                linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(patternLanguageId, patternId)).withSelfRel(),
                linkTo(methodOn(PatternController.class).getPatternContentOfPattern(patternLanguageId, patternId)).withRel("content"),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));

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
