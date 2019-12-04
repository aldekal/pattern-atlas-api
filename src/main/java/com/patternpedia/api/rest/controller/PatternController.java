package com.patternpedia.api.rest.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.PatternViewPattern;
import com.patternpedia.api.entities.UndirectedEdge;
import com.patternpedia.api.exception.DirectedEdgeNotFoundException;
import com.patternpedia.api.exception.UndirectedEdgeNotFoundException;
import com.patternpedia.api.rest.model.PatternContentModel;
import com.patternpedia.api.rest.model.PatternModel;
import com.patternpedia.api.service.PatternLanguageService;
import com.patternpedia.api.service.PatternRelationDescriptorService;
import com.patternpedia.api.service.PatternService;
import com.patternpedia.api.service.PatternViewService;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.validation.Valid;
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
    private PatternRelationDescriptorService patternRelationDescriptorService;
    private ObjectMapper objectMapper;

    public PatternController(PatternService patternService,
                             PatternLanguageService patternLanguageService,
                             PatternViewService patternViewService,
                             PatternRelationDescriptorService patternRelationDescriptorService,
                             ObjectMapper objectMapper) {
        this.patternService = patternService;
        this.patternLanguageService = patternLanguageService;
        this.patternViewService = patternViewService;
        this.patternRelationDescriptorService = patternRelationDescriptorService;
        this.objectMapper = objectMapper;
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

    List<Link> getPatternLinks(Pattern pattern) {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(pattern.getPatternLanguage().getId(), pattern.getId())).withSelfRel()
                .andAffordance(afford(methodOn(PatternController.class).updatePatternViaPut(pattern.getPatternLanguage().getId(), pattern.getId(), null)))
                .andAffordance(afford(methodOn(PatternController.class).deletePatternOfPatternLanguage(pattern.getPatternLanguage().getId(), pattern.getId()))));
        links.add(linkTo(methodOn(PatternController.class).getPatternContentOfPattern(pattern.getPatternLanguage().getId(), pattern.getId())).withRel("content"));
        links.add(linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(pattern.getPatternLanguage().getId())).withRel("patternLanguage"));

        if (null != pattern.getPatternViews()) {
            for (PatternViewPattern patternViewPattern : pattern.getPatternViews()) {
                links.add(linkTo(methodOn(PatternViewController.class).getPatternViewById(patternViewPattern.getPatternView().getId())).withRel("patternView"));
            }
        }

        return links;
    }

    List<Link> getPatternLinksForPatternLanguageRoute(Pattern pattern, UUID patternLanguageId) {
        List<Link> links = this.getPatternLinks(pattern);

        List<DirectedEdge> outgoingEdges;
        try {
            outgoingEdges = this.patternRelationDescriptorService.findDirectedEdgeBySource(pattern);
        } catch (DirectedEdgeNotFoundException ex) {
            outgoingEdges = Collections.emptyList();
        }
        if (null != outgoingEdges) {
            for (DirectedEdge directedEdge : outgoingEdges) {
                if (null != directedEdge.getPatternLanguage() && directedEdge.getPatternLanguage().getId().equals(patternLanguageId)) {
                    // edge is part of pattern language, thus reference the route to edge in pattern language
                    links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                            .getDirectedEdgeOfPatternLanguageById(directedEdge.getPatternLanguage().getId(), directedEdge.getId())).withRel("outgoingDirectedEdges"));
                }
            }
        }

        List<DirectedEdge> ingoingEdges;
        try {
            ingoingEdges = this.patternRelationDescriptorService.findDirectedEdgeByTarget(pattern);
        } catch (DirectedEdgeNotFoundException ex) {
            ingoingEdges = Collections.emptyList();
        }
        if (null != ingoingEdges) {
            for (DirectedEdge directedEdge : ingoingEdges) {
                if (null != directedEdge.getPatternLanguage() && directedEdge.getPatternLanguage().getId().equals(patternLanguageId)) {
                    // edge is part of pattern language, thus reference the route to edge in pattern language
                    links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                            .getDirectedEdgeOfPatternLanguageById(directedEdge.getPatternLanguage().getId(), directedEdge.getId())).withRel("ingoingDirectedEdges"));
                }
            }
        }

        List<UndirectedEdge> undirectedEdges;
        try {
            undirectedEdges = this.patternRelationDescriptorService.findUndirectedEdgeByPattern(pattern);
        } catch (UndirectedEdgeNotFoundException ex) {
            undirectedEdges = Collections.emptyList();
        }
        if (null != undirectedEdges) {
            for (UndirectedEdge undirectedEdge : undirectedEdges) {
                if (null != undirectedEdge.getPatternLanguage() && undirectedEdge.getPatternLanguage().getId().equals(patternLanguageId)) {
                    // edge is part of pattern language, thus reference the route to edge in pattern language
                    links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                            .getUndirectedEdgeOfPatternLanguageById(undirectedEdge.getPatternLanguage().getId(), undirectedEdge.getId())).withRel("undirectedEdges"));
                }
            }
        }

        return links;
    }

    List<Link> getPatternLinksForPatternViewRoute(Pattern pattern, UUID patternViewId) {
        List<Link> links = this.getPatternLinks(pattern);

        List<DirectedEdge> outgoingEdges;
        try {
            outgoingEdges = this.patternRelationDescriptorService.findDirectedEdgeBySource(pattern);
        } catch (DirectedEdgeNotFoundException ex) {
            outgoingEdges = Collections.emptyList();
        }
        if (null != outgoingEdges) {
            for (DirectedEdge directedEdge : outgoingEdges) {
                if (null != directedEdge.getPatternViews()) {
                    // edge is part of pattern view, thus we reference the pattern view route
                    List<Link> newLinks = directedEdge.getPatternViews().stream()
                            .filter(patternViewDirectedEdge -> patternViewDirectedEdge.getPatternView().getId().equals(patternViewId))
                            .map(patternViewDirectedEdge -> linkTo(methodOn(PatternRelationDescriptorController.class)
                                    .getDirectedEdgeOfPatternViewById(patternViewDirectedEdge.getPatternView().getId(), patternViewDirectedEdge.getDirectedEdge().getId())).withRel("outgoingDirectedEdges")
                            ).collect(Collectors.toList());
                    links.addAll(newLinks);
                }
            }
        }

        List<DirectedEdge> ingoingEdges;
        try {
            ingoingEdges = this.patternRelationDescriptorService.findDirectedEdgeByTarget(pattern);
        } catch (DirectedEdgeNotFoundException ex) {
            ingoingEdges = Collections.emptyList();
        }
        if (null != ingoingEdges) {
            for (DirectedEdge directedEdge : ingoingEdges) {
                if (null != directedEdge.getPatternViews()) {
                    // edge is part of pattern view, thus we reference the pattern view route
                    List<Link> newLinks = directedEdge.getPatternViews().stream()
                            .filter(patternViewDirectedEdge -> patternViewDirectedEdge.getPatternView().getId().equals(patternViewId))
                            .map(patternViewDirectedEdge -> linkTo(methodOn(PatternRelationDescriptorController.class)
                                    .getDirectedEdgeOfPatternViewById(patternViewDirectedEdge.getPatternView().getId(), patternViewDirectedEdge.getDirectedEdge().getId())).withRel("ingoingDirectedEdges")
                            ).collect(Collectors.toList());
                    links.addAll(newLinks);
                }
            }
        }

        List<UndirectedEdge> undirectedEdges;
        try {
            undirectedEdges = this.patternRelationDescriptorService.findUndirectedEdgeByPattern(pattern);
        } catch (UndirectedEdgeNotFoundException ex) {
            undirectedEdges = Collections.emptyList();
        }
        if (null != undirectedEdges) {
            for (UndirectedEdge undirectedEdge : undirectedEdges) {
                if (null != undirectedEdge.getPatternViews()) {
                    // edge is part of pattern view, thus we reference the pattern view route
                    List<Link> newLinks = undirectedEdge.getPatternViews().stream()
                            .filter(patternViewUndirectedEdge -> patternViewUndirectedEdge.getPatternView().getId().equals(patternViewId))
                            .map(patternViewUndirectedEdge -> linkTo(methodOn(PatternRelationDescriptorController.class)
                                    .getUndirectedEdgeOfPatternViewById(patternViewUndirectedEdge.getPatternView().getId(), patternViewUndirectedEdge.getUndirectedEdge().getId())).withRel("undirectedEdges")
                            ).collect(Collectors.toList());
                    links.addAll(newLinks);
                }
            }
        }

        List<DirectedEdge> outgoingFromPatternLanguage;
        try {
            outgoingFromPatternLanguage = this.patternRelationDescriptorService.findDirectedEdgeBySource(pattern);
        } catch (DirectedEdgeNotFoundException ex) {
            outgoingFromPatternLanguage = Collections.emptyList();
        }
        if (null != outgoingFromPatternLanguage) {
            for (DirectedEdge directedEdge : outgoingFromPatternLanguage) {
                if (null != directedEdge.getPatternLanguage() && directedEdge.getPatternLanguage().getId().equals(pattern.getPatternLanguage().getId())) {
                    // edge is part of pattern language, thus reference the route to edge in pattern language
                    links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                            .getDirectedEdgeOfPatternLanguageById(directedEdge.getPatternLanguage().getId(), directedEdge.getId())).withRel("outgoingDirectedEdgesFromPatternLanguage"));
                }
            }
        }

        List<DirectedEdge> ingoingFromPatternLanguage;
        try {
            ingoingFromPatternLanguage = this.patternRelationDescriptorService.findDirectedEdgeByTarget(pattern);
        } catch (DirectedEdgeNotFoundException ex) {
            ingoingFromPatternLanguage = Collections.emptyList();
        }
        if (null != ingoingFromPatternLanguage) {
            for (DirectedEdge directedEdge : ingoingFromPatternLanguage) {
                if (null != directedEdge.getPatternLanguage() && directedEdge.getPatternLanguage().getId().equals(pattern.getPatternLanguage().getId())) {
                    // edge is part of pattern language, thus reference the route to edge in pattern language
                    links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                            .getDirectedEdgeOfPatternLanguageById(directedEdge.getPatternLanguage().getId(), directedEdge.getId())).withRel("ingoingDirectedEdgesFromPatternLanguage"));
                }
            }
        }

        List<UndirectedEdge> undirectedFromPatternLanguage;
        try {
            undirectedFromPatternLanguage = this.patternRelationDescriptorService.findUndirectedEdgeByPattern(pattern);
        } catch (UndirectedEdgeNotFoundException ex) {
            undirectedFromPatternLanguage = Collections.emptyList();
        }
        if (null != undirectedFromPatternLanguage) {
            for (UndirectedEdge undirectedEdge : undirectedFromPatternLanguage) {
                if (null != undirectedEdge.getPatternLanguage() && undirectedEdge.getPatternLanguage().getId().equals(pattern.getPatternLanguage().getId())) {
                    // edge is part of pattern language, thus reference the route to edge in pattern language
                    links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                            .getUndirectedEdgeOfPatternLanguageById(undirectedEdge.getPatternLanguage().getId(), undirectedEdge.getId())).withRel("undirectedEdgesFromPatternLanguage"));
                }
            }
        }

        return links;
    }

    @GetMapping(value = "/patternLanguages/{patternLanguageId}/patterns")
    CollectionModel<EntityModel<PatternModel>> getPatternsOfPatternLanguage(@PathVariable UUID patternLanguageId) {
        List<EntityModel<PatternModel>> patterns = this.patternLanguageService.getPatternsOfPatternLanguage(patternLanguageId).stream()
                .map(PatternModel::from)
                .map(patternModel -> new EntityModel<>(patternModel,
                        getPatternLinksForPatternLanguageRoute(patternModel.getPattern(), patternLanguageId)))
                .collect(Collectors.toList());

        return new CollectionModel<>(patterns, getPatternLanguagePatternCollectionLinks(patternLanguageId));
    }

    @GetMapping(value = "/patternViews/{patternViewId}/patterns")
    CollectionModel<EntityModel<PatternModel>> getPatternsOfPatternView(@PathVariable UUID patternViewId) {
        List<EntityModel<PatternModel>> patterns = this.patternViewService.getPatternsOfPatternView(patternViewId).stream()
                .map(PatternModel::from)
                .map(patternModel -> new EntityModel<>(patternModel, getPatternLinksForPatternViewRoute(patternModel.getPattern(), patternViewId)))
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

    @GetMapping(value = "/patternViews/{patternViewId}/patterns/{patternId}")
    EntityModel<Pattern> getPatternOfPatternViewById(@PathVariable UUID patternViewId, @PathVariable UUID patternId) {
        Pattern pattern = this.patternViewService.getPatternOfPatternViewById(patternViewId, patternId);

        return new EntityModel<>(pattern, getPatternLinksForPatternViewRoute(pattern, patternViewId));
    }

    @DeleteMapping(value = "/patternViews/{patternViewId}/patterns/{patternId}")
    ResponseEntity<?> removePatternFromView(@PathVariable UUID patternViewId, @PathVariable UUID patternId) {
        this.patternViewService.removePatternFromPatternView(patternViewId, patternId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/patternLanguages/{patternLanguageId}/patterns")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> addPatternToPatternLanguage(@PathVariable UUID patternLanguageId, @Valid @RequestBody Pattern pattern) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);

        pattern = this.patternLanguageService.createPatternAndAddToPatternLanguage(patternLanguageId, pattern);

        return ResponseEntity.created(linkTo(methodOn(PatternController.class)
                .getPatternOfPatternLanguageById(patternLanguageId, pattern.getId())).toUri()).build();
    }

    @GetMapping(value = "/patternLanguages/{patternLanguageId}/patterns/{patternId}")
    EntityModel<Pattern> getPatternOfPatternLanguageById(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId) {
        Pattern pattern = this.patternLanguageService.getPatternOfPatternLanguageById(patternLanguageId, patternId);
        return new EntityModel<>(pattern, getPatternLinksForPatternLanguageRoute(pattern, patternLanguageId));
    }

    @PutMapping(value = "/patternLanguages/{patternLanguageId}/patterns/{patternId}")
    EntityModel<Pattern> updatePatternViaPut(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId, @Valid @RequestBody Pattern pattern) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);
        Pattern persistedVersion = this.patternService.getPatternById(patternId);
        // Remark: At the moment we do not support changing name, uri of a pattern
        persistedVersion.setIconUrl(pattern.getIconUrl());
        persistedVersion.setContent(pattern.getContent());

        pattern = this.patternService.updatePattern(persistedVersion);
        return new EntityModel<>(pattern,
                linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(patternLanguageId, patternId)).withSelfRel(),
                linkTo(methodOn(PatternController.class).getPatternContentOfPattern(patternLanguageId, patternId)).withRel("content"),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
    }

    @DeleteMapping(value = "/patternLanguages/{patternLanguageId}/patterns/{patternId}")
    ResponseEntity<?> deletePatternOfPatternLanguage(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId) {
        this.patternLanguageService.deletePatternOfPatternLanguage(patternLanguageId, patternId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/patternLanguages/{patternLanguageId}/patterns/{patternId}/content")
    EntityModel<Object> getPatternContentOfPattern(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId) {

        Pattern pattern = this.patternLanguageService.getPatternOfPatternLanguageById(patternLanguageId, patternId);
        PatternContentModel model = new PatternContentModel();

        if (null == pattern.getContent()) {
            model.setContent(this.objectMapper.createObjectNode());
        } else {
            model.setContent(pattern.getContent());
        }

        return new EntityModel<>(model,
                linkTo(methodOn(PatternController.class).getPatternContentOfPattern(patternLanguageId, patternId)).withSelfRel(),
                linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(patternLanguageId, patternId)).withRel("pattern"),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
    }
}
