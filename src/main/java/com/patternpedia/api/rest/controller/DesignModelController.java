package com.patternpedia.api.rest.controller;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.designmodel.DesignModel;
import com.patternpedia.api.entities.designmodel.DesignModelPatternInstance;
import com.patternpedia.api.rest.model.GraphPatternModel;
import com.patternpedia.api.service.DesignModelService;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.text.CaseUtils;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
@CommonsLog
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "/designModels", produces = "application/hal+json")
public class DesignModelController {

    private DesignModelService designModelService;
    private ObjectCodec objectMapper;


    public DesignModelController(DesignModelService designModelService, ObjectMapper objectMapper) {
        this.designModelService = designModelService;
        this.objectMapper = objectMapper;
    }


    private static List<Link> getDesignModelCollectionLinks() {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(DesignModelController.class).getDesignModels()).withSelfRel()
                .andAffordance(afford(methodOn(DesignModelController.class).createDesignModel(null))));

        links.add(linkTo(methodOn(DesignModelController.class).getDesignModel(null)).withRel("designModel"));

        return links;
    }


    private static List<Link> getDesignModelLinks(DesignModel designModel) {
        List<Link> links = new ArrayList<>();

        links.add(
                linkTo(methodOn(DesignModelController.class).getDesignModel(designModel.getId())).withSelfRel()
                        .andAffordance(afford(methodOn(DesignModelController.class).putDesignModel(designModel.getId(), null)))
                        .andAffordance(afford(methodOn(DesignModelController.class).deleteDesignModel(designModel.getId())))
        );
        links.add(linkTo(methodOn(DesignModelController.class).getDesignModels()).withRel("designModels"));
        links.add(linkTo(methodOn(DesignModelController.class).getDesignModelPatternInstances(designModel.getId())).withRel("patterns"));
//        links.add(linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgesOfView(patternView.getId())).withRel("directedEdges"));
//        links.add(linkTo(methodOn(PatternRelationDescriptorController.class).getUndirectedEdgesOfView(patternView.getId())).withRel("undirectedEdges"));

        return links;
    }


    // TODO currently this is a duplicate from PatternController, may generalize this and move it to a utility class, etc.
    List<Link> getPatternLinksForDesignModelRoute(Pattern pattern, UUID patternViewId) {
        List<Link> links = Collections.emptyList();

//        List<Link> links = this.getPatternLinks(pattern);
//
//        List<DirectedEdge> outgoingEdges;
//        try {
//            outgoingEdges = this.patternRelationDescriptorService.findDirectedEdgeBySource(pattern);
//        } catch (DirectedEdgeNotFoundException ex) {
//            outgoingEdges = Collections.emptyList();
//        }
//        if (null != outgoingEdges) {
//            for (DirectedEdge directedEdge : outgoingEdges) {
//                if (null != directedEdge.getPatternViews()) {
//                    // edge is part of pattern view, thus we reference the pattern view route
//                    List<Link> newLinks = directedEdge.getPatternViews().stream()
//                            .filter(patternViewDirectedEdge -> patternViewDirectedEdge.getPatternView().getId().equals(patternViewId))
//                            .map(patternViewDirectedEdge -> linkTo(methodOn(PatternRelationDescriptorController.class)
//                                    .getDirectedEdgeOfPatternViewById(patternViewDirectedEdge.getPatternView().getId(), patternViewDirectedEdge.getDirectedEdge().getId())).withRel("outgoingDirectedEdges")
//                            ).collect(Collectors.toList());
//                    links.addAll(newLinks);
//                }
//            }
//        }
//
//        List<DirectedEdge> ingoingEdges;
//        try {
//            ingoingEdges = this.patternRelationDescriptorService.findDirectedEdgeByTarget(pattern);
//        } catch (DirectedEdgeNotFoundException ex) {
//            ingoingEdges = Collections.emptyList();
//        }
//        if (null != ingoingEdges) {
//            for (DirectedEdge directedEdge : ingoingEdges) {
//                if (null != directedEdge.getPatternViews()) {
//                    // edge is part of pattern view, thus we reference the pattern view route
//                    List<Link> newLinks = directedEdge.getPatternViews().stream()
//                            .filter(patternViewDirectedEdge -> patternViewDirectedEdge.getPatternView().getId().equals(patternViewId))
//                            .map(patternViewDirectedEdge -> linkTo(methodOn(PatternRelationDescriptorController.class)
//                                    .getDirectedEdgeOfPatternViewById(patternViewDirectedEdge.getPatternView().getId(), patternViewDirectedEdge.getDirectedEdge().getId())).withRel("ingoingDirectedEdges")
//                            ).collect(Collectors.toList());
//                    links.addAll(newLinks);
//                }
//            }
//        }
//
//        List<UndirectedEdge> undirectedEdges;
//        try {
//            undirectedEdges = this.patternRelationDescriptorService.findUndirectedEdgeByPattern(pattern);
//        } catch (UndirectedEdgeNotFoundException ex) {
//            undirectedEdges = Collections.emptyList();
//        }
//        if (null != undirectedEdges) {
//            for (UndirectedEdge undirectedEdge : undirectedEdges) {
//                if (null != undirectedEdge.getPatternViews()) {
//                    // edge is part of pattern view, thus we reference the pattern view route
//                    List<Link> newLinks = undirectedEdge.getPatternViews().stream()
//                            .filter(patternViewUndirectedEdge -> patternViewUndirectedEdge.getPatternView().getId().equals(patternViewId))
//                            .map(patternViewUndirectedEdge -> linkTo(methodOn(PatternRelationDescriptorController.class)
//                                    .getUndirectedEdgeOfPatternViewById(patternViewUndirectedEdge.getPatternView().getId(), patternViewUndirectedEdge.getUndirectedEdge().getId())).withRel("undirectedEdges")
//                            ).collect(Collectors.toList());
//                    links.addAll(newLinks);
//                }
//            }
//        }
//
//        List<DirectedEdge> outgoingFromPatternLanguage;
//        try {
//            outgoingFromPatternLanguage = this.patternRelationDescriptorService.findDirectedEdgeBySource(pattern);
//        } catch (DirectedEdgeNotFoundException ex) {
//            outgoingFromPatternLanguage = Collections.emptyList();
//        }
//        if (null != outgoingFromPatternLanguage) {
//            for (DirectedEdge directedEdge : outgoingFromPatternLanguage) {
//                if (null != directedEdge.getPatternLanguage() && directedEdge.getPatternLanguage().getId().equals(pattern.getPatternLanguage().getId())) {
//                    // edge is part of pattern language, thus reference the route to edge in pattern language
//                    links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
//                            .getDirectedEdgeOfPatternLanguageById(directedEdge.getPatternLanguage().getId(), directedEdge.getId())).withRel("outgoingDirectedEdgesFromPatternLanguage"));
//                }
//            }
//        }
//
//        List<DirectedEdge> ingoingFromPatternLanguage;
//        try {
//            ingoingFromPatternLanguage = this.patternRelationDescriptorService.findDirectedEdgeByTarget(pattern);
//        } catch (DirectedEdgeNotFoundException ex) {
//            ingoingFromPatternLanguage = Collections.emptyList();
//        }
//        if (null != ingoingFromPatternLanguage) {
//            for (DirectedEdge directedEdge : ingoingFromPatternLanguage) {
//                if (null != directedEdge.getPatternLanguage() && directedEdge.getPatternLanguage().getId().equals(pattern.getPatternLanguage().getId())) {
//                    // edge is part of pattern language, thus reference the route to edge in pattern language
//                    links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
//                            .getDirectedEdgeOfPatternLanguageById(directedEdge.getPatternLanguage().getId(), directedEdge.getId())).withRel("ingoingDirectedEdgesFromPatternLanguage"));
//                }
//            }
//        }
//
//        List<UndirectedEdge> undirectedFromPatternLanguage;
//        try {
//            undirectedFromPatternLanguage = this.patternRelationDescriptorService.findUndirectedEdgeByPattern(pattern);
//        } catch (UndirectedEdgeNotFoundException ex) {
//            undirectedFromPatternLanguage = Collections.emptyList();
//        }
//        if (null != undirectedFromPatternLanguage) {
//            for (UndirectedEdge undirectedEdge : undirectedFromPatternLanguage) {
//                if (null != undirectedEdge.getPatternLanguage() && undirectedEdge.getPatternLanguage().getId().equals(pattern.getPatternLanguage().getId())) {
//                    // edge is part of pattern language, thus reference the route to edge in pattern language
//                    links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
//                            .getUndirectedEdgeOfPatternLanguageById(undirectedEdge.getPatternLanguage().getId(), undirectedEdge.getId())).withRel("undirectedEdgesFromPatternLanguage"));
//                }
//            }
//        }

        return links;
    }


    static List<Link> getDesignModelPatternInstanceCollectionLinks(UUID designModelId) {
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(DesignModelController.class).getDesignModelPatternInstances(designModelId)).withSelfRel()
                .andAffordance(afford(methodOn(DesignModelController.class).addDesignModelPatternInstance(designModelId, null))));
        links.add(linkTo(methodOn(DesignModelController.class).getDesignModel(designModelId)).withRel("designModel"));
        links.add(linkTo(methodOn(DesignModelController.class).getDesignModelPatternInstances(designModelId)).withRel("patterns"));
        return links;
    }


    @GetMapping("")
    public CollectionModel<EntityModel<DesignModel>> getDesignModels() {

        List<EntityModel<DesignModel>> patternViews = this.designModelService.getAllDesignModels()
                .stream()
                .map(patternView -> new EntityModel<>(patternView,
                        getDesignModelLinks(patternView)))
                .collect(Collectors.toList());

        return new CollectionModel<>(patternViews, getDesignModelCollectionLinks());
    }


    @PostMapping("")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createDesignModel(@RequestBody DesignModel designModel) {
        String nameAsCamelCase = CaseUtils.toCamelCase(designModel.getName(), false);
        String uri = String.format("https://patternpedia.org/designModels/%s", nameAsCamelCase);
        designModel.setUri(uri);

        DesignModel createdDesignModel = this.designModelService.createDesignModel(designModel);

        return ResponseEntity.created(linkTo(methodOn(DesignModelController.class)
                .getDesignModel(createdDesignModel.getId())).toUri()).build();
    }


    @GetMapping("/{designModelId}")
    public EntityModel<DesignModel> getDesignModel(@PathVariable UUID designModelId) {
        DesignModel patternView = this.designModelService.getDesignModel(designModelId);

        return new EntityModel<>(patternView, getDesignModelLinks(patternView));
    }


    @PutMapping("/{designModelId}")
    public ResponseEntity<?> putDesignModel(@PathVariable UUID designModelId, @RequestBody DesignModel designModel) {
//        patternView = this.patternViewService.updateDesignModel(patternView);

        return ResponseEntity.ok(designModel);
    }


    @DeleteMapping("/{designModelId}")
    public ResponseEntity<?> deleteDesignModel(@PathVariable UUID designModelId) {
//        this.patternViewService.deleteDesignModel(designModelId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{designModelId}/patterns")
    CollectionModel<EntityModel<GraphPatternModel>> getDesignModelPatternInstances(@PathVariable UUID designModelId) {
        List<DesignModelPatternInstance> patternInstances = this.designModelService.getDesignModel(designModelId).getPatterns();

        List<EntityModel<GraphPatternModel>> patterns = patternInstances.stream()
                .map(GraphPatternModel::from)
                .map(patternModel -> new EntityModel<>(patternModel))// TODO, getPatternLinksForDesignModelRoute(patternModel, designModelId)))
                .collect(Collectors.toList());
        return new CollectionModel<>(patterns, getDesignModelPatternInstanceCollectionLinks(designModelId));
    }


    @PostMapping("/{designModelId}/patterns")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addDesignModelPatternInstance(@PathVariable UUID designModelId, @RequestBody Pattern pattern) {
        this.designModelService.addPatternToDesignModel(designModelId, pattern.getId());
        return ResponseEntity.created(linkTo(methodOn(PatternController.class) // TODO fix controller
                .getPatternOfPatternViewById(designModelId, pattern.getId())).toUri()).build();
    }


    @DeleteMapping("/{designModelId}/patterns")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> deleteDesignModelPatternInstance(@PathVariable UUID designModelId, @RequestBody Pattern pattern) {
        this.designModelService.addPatternToDesignModel(designModelId, pattern.getId());
        return ResponseEntity.created(linkTo(methodOn(PatternController.class) // TODO fix controller
                .getPatternOfPatternViewById(designModelId, pattern.getId())).toUri()).build();
    }
}
