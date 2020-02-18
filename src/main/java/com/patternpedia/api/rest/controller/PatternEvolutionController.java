package com.patternpedia.api.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patternpedia.api.entities.*;
import com.patternpedia.api.exception.DirectedEdgeNotFoundException;
import com.patternpedia.api.exception.UndirectedEdgeNotFoundException;
import com.patternpedia.api.repositories.PatternEvolutionRepository;
import com.patternpedia.api.rest.model.PatternContentModel;
import com.patternpedia.api.rest.model.PatternEvolutionModel;
import com.patternpedia.api.rest.model.PatternLanguageModel;
import com.patternpedia.api.rest.model.PatternModel;
import com.patternpedia.api.service.*;
import org.apache.commons.text.CaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
//@CrossOrigin(allowedHeaders = "*", origins = "*")
//@RequestMapping(produces = "application/hal+json")
public class PatternEvolutionController {

    private final PatternEvolutionRepository repo;
    Logger logger = LoggerFactory.getLogger(PatternEvolutionController.class);

    private PatternEvolutionService patternEvolutionService;
    private PatternLanguageService patternLanguageService;
//    private PatternViewService patternViewService;
//    private PatternRelationDescriptorService patternRelationDescriptorService;
    private ObjectMapper objectMapper;

    public PatternEvolutionController(
            PatternEvolutionService patternEvolutionService,
            PatternLanguageService patternLanguageService,
//            PatternViewService patternViewService,
//            PatternRelationDescriptorService patternRelationDescriptorService,
            ObjectMapper objectMapper,
            PatternEvolutionRepository repo
    ) {
        this.patternEvolutionService = patternEvolutionService;
        this.patternLanguageService = patternLanguageService;
//        this.patternViewService = patternViewService;
//        this.patternRelationDescriptorService = patternRelationDescriptorService;
        this.objectMapper = objectMapper;
        this.repo = repo;
    }

    @GetMapping(value = "/getAllPatternEvolutions")
    List<PatternEvolution> all() {
        PatternEvolution p = new PatternEvolution("Herbert");
        logger.info(p.toString());
        repo.save(p);
        return repo.findAll();
//    CollectionModel<PatternEvolution> getAllPatternLanguages() {
//        List<PatternEvolution> patternEvolutions = this.patternEvolutionService.getPatternEvolutions();
////                .stream()
////                .map(PatternEvolutionModel::from)
////                .map(patternEvolutionModel -> new EntityModel<>(patternEvolutionModel,
////                        getPatternLanguageLinks(patternEvolutionModel.getId())))
////                .collect(Collectors.toList());
//
//        return new CollectionModel<>(patternEvolutions);
    }

    @PostMapping
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    PatternEvolution newPatternEvolution(@RequestBody PatternEvolution patternEvolution) {
        PatternEvolution p = new PatternEvolution("Herbert");
        logger.info(patternEvolution.toString());
        return repo.save(p);
//        return repo.save(patternEvolution);
//        return this.patternEvolutionService.createPatternEvolution(patternEvolution);
    }

//    @PostMapping(value = "/patternLanguages/{patternLanguageId}/patternEvolution")
////    @CrossOrigin(exposedHeaders = "Location")
//    @ResponseStatus(HttpStatus.CREATED)
//    ResponseEntity<?> addPatternToPatternLanguage(@PathVariable UUID patternLanguageId, PatternEvolution patternEvolution) {
////        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);
////        if (null == pattern.getUri()) {
////            pattern.setUri(patternLanguage.getUri() + '/' + CaseUtils.toCamelCase(pattern.getName(), false));
////        }
//        this.patternEvolutionService.createPatternEvolution(patternEvolution);
////        pattern = this.patternLanguageService.createPatternAndAddToPatternLanguage(patternLanguageId, pattern);
//
//        return null;
////        return ResponseEntity.created(linkTo(methodOn(PatternController.class)
////                .getPatternOfPatternLanguageById(patternLanguageId, pattern.getId())).toUri()).build();
//    }
}

//    @GetMapping(value = "/patternLanguages/{patternLanguageId}/patterns")
//    CollectionModel<EntityModel<PatternModel>> getPatternsOfPatternLanguage(@PathVariable UUID patternLanguageId) {
//        List<EntityModel<PatternModel>> patterns = this.patternLanguageService.getPatternsOfPatternLanguage(patternLanguageId).stream()
//                .map(PatternModel::from)
//                .map(patternModel -> new EntityModel<>(patternModel,
//                        getPatternLinksForPatternLanguageRoute(patternModel.getPattern(), patternLanguageId)))
//                .collect(Collectors.toList());
//
//        return new CollectionModel<>(patterns, getPatternLanguagePatternCollectionLinks(patternLanguageId));
//    }
//
//    @GetMapping(value = "/patternViews/{patternViewId}/patterns")
//    CollectionModel<EntityModel<PatternModel>> getPatternsOfPatternView(@PathVariable UUID patternViewId) {
//        List<EntityModel<PatternModel>> patterns = this.patternViewService.getPatternsOfPatternView(patternViewId).stream()
//                .map(PatternModel::from)
//                .map(patternModel -> new EntityModel<>(patternModel, getPatternLinksForPatternViewRoute(patternModel.getPattern(), patternViewId)))
//                .collect(Collectors.toList());
//        return new CollectionModel<>(patterns, getPatternViewPatternCollectionLinks(patternViewId));
//    }
//
//    @PostMapping(value = "/patternViews/{patternViewId}/patterns")
//    @CrossOrigin(exposedHeaders = "Location")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<?> addPatternToPatternView(@PathVariable UUID patternViewId, @RequestBody Pattern pattern) {
//        this.patternViewService.addPatternToPatternView(patternViewId, pattern.getId());
//        return ResponseEntity.created(linkTo(methodOn(PatternEvolutionController.class)
//                .getPatternOfPatternViewById(patternViewId, pattern.getId())).toUri()).build();
//    }
//
//    @GetMapping(value = "/patternViews/{patternViewId}/patterns/{patternId}")
//    EntityModel<Pattern> getPatternOfPatternViewById(@PathVariable UUID patternViewId, @PathVariable UUID patternId) {
//        Pattern pattern = this.patternViewService.getPatternOfPatternViewById(patternViewId, patternId);
//
//        return new EntityModel<>(pattern, getPatternLinksForPatternViewRoute(pattern, patternViewId));
//    }
//
//    @DeleteMapping(value = "/patternViews/{patternViewId}/patterns/{patternId}")
//    ResponseEntity<?> removePatternFromView(@PathVariable UUID patternViewId, @PathVariable UUID patternId) {
//        this.patternViewService.removePatternFromPatternView(patternViewId, patternId);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping(value = "/patternLanguages/{patternLanguageId}/patterns")
//    @CrossOrigin(exposedHeaders = "Location")
//    @ResponseStatus(HttpStatus.CREATED)
//    ResponseEntity<?> addPatternToPatternLanguage(@PathVariable UUID patternLanguageId, @Valid @RequestBody Pattern pattern) {
//        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);
//        if (null == pattern.getUri()) {
//            pattern.setUri(patternLanguage.getUri() + '/' + CaseUtils.toCamelCase(pattern.getName(), false));
//        }
//
//        pattern = this.patternLanguageService.createPatternAndAddToPatternLanguage(patternLanguageId, pattern);
//
//        return ResponseEntity.created(linkTo(methodOn(PatternEvolutionController.class)
//                .getPatternOfPatternLanguageById(patternLanguageId, pattern.getId())).toUri()).build();
//    }
//
//    @GetMapping(value = "/patternLanguages/{patternLanguageId}/patterns/{patternId}")
//    EntityModel<Pattern> getPatternOfPatternLanguageById(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId) {
//        Pattern pattern = this.patternLanguageService.getPatternOfPatternLanguageById(patternLanguageId, patternId);
//        return new EntityModel<>(pattern, getPatternLinksForPatternLanguageRoute(pattern, patternLanguageId));
//    }
//
//    @PutMapping(value = "/patternLanguages/{patternLanguageId}/patterns/{patternId}")
//    EntityModel<Pattern> updatePatternViaPut(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId, @Valid @RequestBody Pattern pattern) {
//        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);
//        Pattern persistedVersion = this.patternService.getPatternById(patternId);
//        // Remark: At the moment we do not support changing name, uri of a pattern
//        persistedVersion.setIconUrl(pattern.getIconUrl());
//        persistedVersion.setContent(pattern.getContent());
//
//        pattern = this.patternService.updatePattern(persistedVersion);
//        return new EntityModel<>(pattern,
//                linkTo(methodOn(PatternEvolutionController.class).getPatternOfPatternLanguageById(patternLanguageId, patternId)).withSelfRel(),
//                linkTo(methodOn(PatternEvolutionController.class).getPatternContentOfPattern(patternLanguageId, patternId)).withRel("content"),
//                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
//    }
//
//    @DeleteMapping(value = "/patternLanguages/{patternLanguageId}/patterns/{patternId}")
//    ResponseEntity<?> deletePatternOfPatternLanguage(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId) {
//        this.patternLanguageService.deletePatternOfPatternLanguage(patternLanguageId, patternId);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping(value = "/patternLanguages/{patternLanguageId}/patterns/{patternId}/content")
//    EntityModel<Object> getPatternContentOfPattern(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId) {
//
//        Pattern pattern = this.patternLanguageService.getPatternOfPatternLanguageById(patternLanguageId, patternId);
//        PatternContentModel model = new PatternContentModel();
//
//        if (null == pattern.getContent()) {
//            model.setContent(this.objectMapper.createObjectNode());
//        } else {
//            model.setContent(pattern.getContent());
//        }
//
//        return new EntityModel<>(model,
//                linkTo(methodOn(PatternEvolutionController.class).getPatternContentOfPattern(patternLanguageId, patternId)).withSelfRel(),
//                linkTo(methodOn(PatternEvolutionController.class).getPatternOfPatternLanguageById(patternLanguageId, patternId)).withRel("pattern"),
//                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
//    }
//}
