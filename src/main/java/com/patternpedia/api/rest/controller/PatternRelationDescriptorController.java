package com.patternpedia.api.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.UndirectedEdge;
import com.patternpedia.api.rest.model.AddDirectedEdgeToViewRequest;
import com.patternpedia.api.rest.model.AddUndirectedEdgeToViewRequest;
import com.patternpedia.api.rest.model.CreateDirectedEdgeRequest;
import com.patternpedia.api.rest.model.CreateUndirectedEdgeRequest;
import com.patternpedia.api.rest.model.DirectedEdgeModel;
import com.patternpedia.api.rest.model.UndirectedEdgeModel;
import com.patternpedia.api.rest.model.UpdateDirectedEdgeRequest;
import com.patternpedia.api.rest.model.UpdateUndirectedEdgeRequest;
import com.patternpedia.api.service.PatternLanguageService;
import com.patternpedia.api.service.PatternViewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "/", produces = "application/hal+json")
public class PatternRelationDescriptorController {

    private PatternLanguageService patternLanguageService;
    private PatternViewService patternViewService;

    public PatternRelationDescriptorController(PatternLanguageService patternLanguageService,
                                               PatternViewService patternViewService) {
        this.patternLanguageService = patternLanguageService;
        this.patternViewService = patternViewService;
    }

    // Links and Affordances

    // DirectedEdge

    private static List<Link> getDirectedEdgeLinksForPatternLanguageRoute(DirectedEdge directedEdge) {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getDirectedEdgeOfPatternLanguageById(directedEdge.getPatternLanguage().getId(), directedEdge.getId())).withSelfRel()
                .andAffordance(afford(methodOn(PatternRelationDescriptorController.class).removeDirectedEdgeFromPatternLanguage(directedEdge.getPatternLanguage().getId(), directedEdge.getId())))
                .andAffordance(afford(methodOn(PatternRelationDescriptorController.class).putDirectedEdgeToPatternLanguage(directedEdge.getPatternLanguage().getId(), directedEdge.getId(), directedEdge))));

        links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getDirectedEdgesOfPatternLanguage(directedEdge.getPatternLanguage().getId())).withRel("directedEdges"));

        return getNonRouteRelatedLinksOfDirectedEdge(directedEdge, links);
    }

    private static List<Link> getDirectedEdgeLinksForViewRoute(DirectedEdge directedEdge, UUID patternViewId) {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getDirectedEdgeOfPatternViewById(patternViewId, directedEdge.getId())).withSelfRel()
                .andAffordance(afford(methodOn(PatternRelationDescriptorController.class).removeDirectedEdgeFromPatternView(patternViewId, directedEdge.getId())))
                .andAffordance(afford(methodOn(PatternRelationDescriptorController.class).putDirectedEdgeToPatternView(patternViewId, directedEdge.getId(), null))));

        links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getDirectedEdgesOfView(patternViewId)).withRel("directedEdges"));

        return getNonRouteRelatedLinksOfDirectedEdge(directedEdge, links);
    }

    private static List<Link> getDirectedEdgeCollectionResourceLinksForPatternLanguageRoute(UUID patternLanguageId) {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getDirectedEdgesOfPatternLanguage(patternLanguageId)).withSelfRel()
                .andAffordance(afford(methodOn(PatternRelationDescriptorController.class)
                        .addDirectedEdgeToPatternLanguage(patternLanguageId, null))));

        links.add(linkTo(methodOn(PatternLanguageController.class)
                .getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));

        return links;
    }

    private static List<Link> getDirectedEdgeCollectionResourceLinksForViewRoute(UUID patternViewId) {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getDirectedEdgesOfView(patternViewId)).withSelfRel()
                .andAffordance(afford(methodOn(PatternRelationDescriptorController.class)
                        .addDirectedEdgeToView(patternViewId, null))));

        links.add(linkTo(methodOn(PatternViewController.class)
                .getPatternViewById(patternViewId)).withRel("patternView"));

        return links;
    }

    private static List<Link> getNonRouteRelatedLinksOfDirectedEdge(DirectedEdge directedEdge, List<Link> links) {
        if (null != directedEdge.getPatternViews()) {
            List<Link> newLinks = directedEdge.getPatternViews().parallelStream()
                    .map(patternViewDirectedEdge -> linkTo(methodOn(PatternViewController.class).getPatternViewById(patternViewDirectedEdge.getPatternView().getId())).withRel("patternView")
                    ).collect(Collectors.toList());
            links.addAll(newLinks);
        }

        if (null != directedEdge.getPatternLanguage()) {
            links.add(linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(directedEdge.getPatternLanguage().getId())).withRel("patternLanguage"));
        }

        links.add(linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(directedEdge.getSource().getPatternLanguage().getId(), directedEdge.getSource().getId())).withRel("sourcePattern"));
        links.add(linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(directedEdge.getTarget().getPatternLanguage().getId(), directedEdge.getTarget().getId())).withRel("targetPattern"));

        return links;
    }

    // UndirectedEdge

    private static List<Link> getUndirectedEdgeLinksForPatternLanguageRoute(UndirectedEdge undirectedEdge) {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getUndirectedEdgeOfPatternLanguageById(undirectedEdge.getPatternLanguage().getId(), undirectedEdge.getId())).withSelfRel()
                .andAffordance(afford(methodOn(PatternRelationDescriptorController.class)
                        .removeUndirectedEdgeFromPatternLanguage(undirectedEdge.getPatternLanguage().getId(), undirectedEdge.getId())))
                .andAffordance(afford(methodOn(PatternRelationDescriptorController.class)
                        .putUndirectedEdgeToPatternLanguage(undirectedEdge.getPatternLanguage().getId(), undirectedEdge.getId(), undirectedEdge))));

        links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getUndirectedEdgesOfPatternLanguage(undirectedEdge.getPatternLanguage().getId())).withRel("undirectedEdges"));

        return getNonRouteRelatedLinksOfUndirectedEdge(undirectedEdge, links);
    }

    private static List<Link> getUndirectedEdgeLinksForViewRoute(UUID patternViewId, UndirectedEdge undirectedEdge) {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getUndirectedEdgeOfPatternViewById(patternViewId, undirectedEdge.getId())).withSelfRel()
                .andAffordance(afford(methodOn(PatternRelationDescriptorController.class).removeUndirectedEdgeFromPatternView(patternViewId, undirectedEdge.getId())))
                .andAffordance(afford(methodOn(PatternRelationDescriptorController.class).putUndirectedEdgeToPatternView(patternViewId, undirectedEdge.getId(), null))));

        links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getUndirectedEdgesOfView(patternViewId)).withRel("undirectedEdges"));

        return getNonRouteRelatedLinksOfUndirectedEdge(undirectedEdge, links);
    }

    private static List<Link> getUndirectedEdgeCollectionResourceLinksForPatternLanguageRoute(UUID patternLanguageId) {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getUndirectedEdgesOfPatternLanguage(patternLanguageId)).withSelfRel()
                .andAffordance(afford(methodOn(PatternRelationDescriptorController.class)
                        .addUndirectedEdgeToPatternLanguage(patternLanguageId, null))));

        links.add(linkTo(methodOn(PatternLanguageController.class)
                .getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));

        return links;
    }

    private static List<Link> getUndirectedEdgeCollectionResourceLinksForViewRoute(UUID patternViewId) {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getUndirectedEdgesOfView(patternViewId)).withSelfRel()
                .andAffordance(afford(methodOn(PatternRelationDescriptorController.class)
                        .addUndirectedEdgeToPatternLanguage(patternViewId, null)))
                .andAffordance(afford(methodOn(PatternRelationDescriptorController.class)
                        .addUndirectedEdgeToView(patternViewId, null))));

        links.add(linkTo(methodOn(PatternViewController.class)
                .getPatternViewById(patternViewId)).withRel("patternView"));

        return links;
    }

    private static List<Link> getNonRouteRelatedLinksOfUndirectedEdge(UndirectedEdge undirectedEdge, List<Link> links) {
        if (null != undirectedEdge.getPatternViews()) {
            List<Link> newLinks = undirectedEdge.getPatternViews().stream()
                    .map(patternViewUndirectedEdge -> linkTo(methodOn(PatternViewController.class).getPatternViewById(patternViewUndirectedEdge.getPatternView().getId())).withRel("patternView")
                    ).collect(Collectors.toList());
            links.addAll(newLinks);
        }

        if (null != undirectedEdge.getPatternLanguage()) {
            links.add(linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(undirectedEdge.getPatternLanguage().getId())).withRel("patternLanguage"));
        }

        links.add(linkTo(methodOn(PatternController.class)
                .getPatternOfPatternLanguageById(undirectedEdge.getP1().getPatternLanguage().getId(),
                        undirectedEdge.getP1().getId())).withRel("pattern"));
        links.add(linkTo(methodOn(PatternController.class)
                .getPatternOfPatternLanguageById(undirectedEdge.getP2().getPatternLanguage().getId(),
                        undirectedEdge.getP2().getId())).withRel("pattern"));

        return links;
    }

    // Edges of Pattern Languages

    @Operation(operationId = "addDirectedEdgeToPatternLanguage", responses = {@ApiResponse(responseCode = "201"), @ApiResponse(responseCode = "404")}, description = "Adds directed edge to pattern language")
    @PostMapping(value = "/patternLanguages/{patternLanguageId}/directedEdges")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DirectedEdge> addDirectedEdgeToPatternLanguage(@PathVariable UUID patternLanguageId, @RequestBody CreateDirectedEdgeRequest createDirectedEdgeRequest) {
        DirectedEdge directedEdge = this.patternLanguageService.createDirectedEdgeAndAddToPatternLanguage(patternLanguageId, createDirectedEdgeRequest);

        return ResponseEntity
                .created(linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgeOfPatternLanguageById(patternLanguageId, directedEdge.getId())).toUri())
                .body(directedEdge);
    }

    @Operation(operationId = "getDirectedEdgesOfPatternLanguage", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Get directed edges of pattern language")
    @GetMapping(value = "/patternLanguages/{patternLanguageId}/directedEdges")
    public CollectionModel<EntityModel<DirectedEdgeModel>> getDirectedEdgesOfPatternLanguage(@PathVariable UUID patternLanguageId) {
        List<EntityModel<DirectedEdgeModel>> directedEdges = this.patternLanguageService.getDirectedEdgesOfPatternLanguage(patternLanguageId)
                .parallelStream()
                .map(DirectedEdgeModel::from)
                .map(directedEdgeModel -> new EntityModel<>(directedEdgeModel, getDirectedEdgeLinksForPatternLanguageRoute(directedEdgeModel.getDirectedEdge())))
                .collect(Collectors.toList());
        return new CollectionModel<>(directedEdges, getDirectedEdgeCollectionResourceLinksForPatternLanguageRoute(patternLanguageId));
    }

    @Operation(operationId = "getDirectedEdgeOfPatternLanguageById", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Get directed edge of pattern language")
    @GetMapping(value = "/patternLanguages/{patternLanguageId}/directedEdges/{directedEdgeId}")
    public EntityModel<DirectedEdgeModel> getDirectedEdgeOfPatternLanguageById(@PathVariable UUID patternLanguageId, @PathVariable UUID directedEdgeId) {
        DirectedEdge directedEdge = this.patternLanguageService.getDirectedEdgeOfPatternLanguageById(patternLanguageId, directedEdgeId);
        return new EntityModel<>(DirectedEdgeModel.from(directedEdge), getDirectedEdgeLinksForPatternLanguageRoute(directedEdge));
    }

    @Operation(operationId = "updateDirectedEdgeOfPatternLanguage", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Update directed edge of pattern language")
    @PutMapping(value = "/patternLanguages/{patternLanguageId}/directedEdges/{directedEdgeId}")
    public EntityModel<DirectedEdgeModel> putDirectedEdgeToPatternLanguage(@PathVariable UUID patternLanguageId, @PathVariable UUID directedEdgeId, @RequestBody DirectedEdge directedEdge) {
        directedEdge = this.patternLanguageService.updateDirectedEdgeOfPatternLanguage(patternLanguageId, directedEdge);

        return new EntityModel<>(DirectedEdgeModel.from(directedEdge), getDirectedEdgeLinksForPatternLanguageRoute(directedEdge));
    }

    @Operation(operationId = "removeDirectedEdgeFromPatternLanguage", responses = {@ApiResponse(responseCode = "204"), @ApiResponse(responseCode = "404")}, description = "Remove directed edge of pattern language")
    @DeleteMapping(value = "/patternLanguages/{patternLanguageId}/directedEdges/{directedEdgeId}")
    public ResponseEntity<?> removeDirectedEdgeFromPatternLanguage(@PathVariable UUID patternLanguageId, @PathVariable UUID directedEdgeId) {
        this.patternLanguageService.removeDirectedEdgeFromPatternLanguage(patternLanguageId, directedEdgeId);
        return ResponseEntity.noContent().build();
    }

    @Operation(operationId = "addUndirectedEdgeToPatternLanguage", responses = {@ApiResponse(responseCode = "201"), @ApiResponse(responseCode = "404")}, description = "Add undirected edge to pattern language")
    @PostMapping(value = "/patternLanguages/{patternLanguageId}/undirectedEdges")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UndirectedEdge> addUndirectedEdgeToPatternLanguage(@PathVariable UUID patternLanguageId, @RequestBody CreateUndirectedEdgeRequest undirectedEdgeRequest) {
        UndirectedEdge undirectedEdge = this.patternLanguageService.createUndirectedEdgeAndAddToPatternLanguage(patternLanguageId, undirectedEdgeRequest);

        return ResponseEntity
                .created(linkTo(methodOn(PatternRelationDescriptorController.class).getUndirectedEdgeOfPatternLanguageById(patternLanguageId, undirectedEdge.getId())).toUri())
                .body(undirectedEdge);
    }

    @Operation(operationId = "getUndirectedEdgesOfPatternLanguage", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Get undirected edge of pattern language")
    @GetMapping(value = "/patternLanguages/{patternLanguageId}/undirectedEdges")
    public CollectionModel<EntityModel<UndirectedEdgeModel>> getUndirectedEdgesOfPatternLanguage(@PathVariable UUID patternLanguageId) {
        List<EntityModel<UndirectedEdgeModel>> undirectedEdges = this.patternLanguageService.getUndirectedEdgesOfPatternLanguage(patternLanguageId)
                .stream()
                .map(UndirectedEdgeModel::from)
                .map(undirectedEdgeModel -> new EntityModel<>(undirectedEdgeModel, getUndirectedEdgeLinksForPatternLanguageRoute(undirectedEdgeModel.getUndirectedEdge()))
                ).collect(Collectors.toList());

        return new CollectionModel<>(undirectedEdges, getUndirectedEdgeCollectionResourceLinksForPatternLanguageRoute(patternLanguageId));
    }

    @Operation(operationId = "getUndirectedEdgeOfPatternLanguageById", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Get undirected edge of pattern language by id")
    @GetMapping(value = "/patternLanguages/{patternLanguageId}/undirectedEdges/{undirectedEdgeId}")
    public EntityModel<UndirectedEdgeModel> getUndirectedEdgeOfPatternLanguageById(@PathVariable UUID patternLanguageId, @PathVariable UUID undirectedEdgeId) {
        UndirectedEdge undirectedEdge = this.patternLanguageService.getUndirectedEdgeOfPatternLanguageById(patternLanguageId, undirectedEdgeId);

        return new EntityModel<>(UndirectedEdgeModel.from(undirectedEdge), getUndirectedEdgeLinksForPatternLanguageRoute(undirectedEdge));
    }

    @Operation(operationId = "updateUndirectedEdgeOfPatternLanguageById", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Get undirected edge of pattern language by id")
    @PutMapping(value = "/patternLanguages/{patternLanguageId}/undirectedEdges/{undirectedEdgeId}")
    public EntityModel<UndirectedEdgeModel> putUndirectedEdgeToPatternLanguage(@PathVariable UUID patternLanguageId, @PathVariable UUID undirectedEdgeId, @RequestBody UndirectedEdge undirectedEdge) {
        undirectedEdge = this.patternLanguageService.updateUndirectedEdgeOfPatternLanguage(patternLanguageId, undirectedEdge);

        return new EntityModel<>(UndirectedEdgeModel.from(undirectedEdge), getUndirectedEdgeLinksForPatternLanguageRoute(undirectedEdge));
    }

    @Operation(operationId = "removeUndirectedEdgeFromPatternLanguage", responses = {@ApiResponse(responseCode = "204"), @ApiResponse(responseCode = "404")}, description = "Remove undirected edge of pattern language by id")
    @DeleteMapping(value = "/patternLanguages/{patternLanguageId}/undirectedEdges/{undirectedEdgeId}")
    public ResponseEntity<?> removeUndirectedEdgeFromPatternLanguage(@PathVariable UUID patternLanguageId, @PathVariable UUID undirectedEdgeId) {
        this.patternLanguageService.removeUndirectedEdgeFromPatternLanguage(patternLanguageId, undirectedEdgeId);
        return ResponseEntity.noContent().build();
    }

    // Edges of Views

    @Operation(operationId = "addDirectedEdgeToView", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Adds directed edge to view")
    @PostMapping(value = "/patternViews/{patternViewId}/directedEdges")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addDirectedEdgeToView(@PathVariable UUID patternViewId, @RequestBody AddDirectedEdgeToViewRequest request) {
        if (request.isNewEdge()) {
            DirectedEdge directedEdge = this.patternViewService.createDirectedEdgeAndAddToPatternView(patternViewId, request);
            return ResponseEntity.created(linkTo(methodOn(PatternRelationDescriptorController.class)
                    .getDirectedEdgeOfPatternViewById(patternViewId, directedEdge.getId())).toUri()).body(directedEdge);
        } else {
            this.patternViewService.addDirectedEdgeToPatternView(patternViewId, request.getDirectedEdgeId());
            return ResponseEntity.created(linkTo(methodOn(PatternRelationDescriptorController.class)
                    .getDirectedEdgeOfPatternViewById(patternViewId, request.getDirectedEdgeId())).toUri()).build();
        }
    }

    @Operation(operationId = "getDirectedEdgesOfView", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Retrieve directed edges of view")
    @GetMapping(value = "/patternViews/{patternViewId}/directedEdges")
    public CollectionModel<EntityModel<DirectedEdgeModel>> getDirectedEdgesOfView(@PathVariable UUID patternViewId) {
        List<EntityModel<DirectedEdgeModel>> directedEdgeResources = this.patternViewService.getDirectedEdgesByPatternViewId(patternViewId)
                .stream()
                .map(DirectedEdgeModel::from)
                .map(directedEdgeModel -> new EntityModel<>(directedEdgeModel, getDirectedEdgeLinksForViewRoute(directedEdgeModel.getDirectedEdge(), patternViewId)))
                .collect(Collectors.toList());

        return new CollectionModel<>(directedEdgeResources, getDirectedEdgeCollectionResourceLinksForViewRoute(patternViewId));
    }

    @Operation(operationId = "getDirectedEdgeOfPatternViewById", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Retrieve directed edge of pattern view by id")
    @GetMapping(value = "/patternViews/{patternViewId}/directedEdges/{directedEdgeId}")
    public EntityModel<DirectedEdgeModel> getDirectedEdgeOfPatternViewById(@PathVariable UUID patternViewId, @PathVariable UUID directedEdgeId) {
        DirectedEdge directedEdge = this.patternViewService.getDirectedEdgeOfPatternViewById(patternViewId, directedEdgeId);

        return new EntityModel<>(DirectedEdgeModel.from(directedEdge), getDirectedEdgeLinksForViewRoute(directedEdge, patternViewId));
    }

    @Operation(operationId = "updateDirectedEdgeOfPatternViewById", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Update directed edge of pattern view by id")
    @PutMapping(value = "/patternViews/{patternViewId}/directedEdges/{directedEdgeId}")
    public EntityModel<DirectedEdgeModel> putDirectedEdgeToPatternView(@PathVariable UUID patternViewId, @PathVariable UUID directedEdgeId, @RequestBody UpdateDirectedEdgeRequest request) {
        request.setDirectedEdgeId(directedEdgeId);
        DirectedEdge directedEdge = this.patternViewService.updateDirectedEdgeOfPatternView(patternViewId, request);
        return new EntityModel<>(DirectedEdgeModel.from(directedEdge), getDirectedEdgeLinksForViewRoute(directedEdge, patternViewId));
    }

    @Operation(operationId = "removeDirectedEdgeFromPatternView", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Remove directed edge of pattern view by id")
    @DeleteMapping(value = "/patternViews/{patternViewId}/directedEdges/{directedEdgeId}")
    public ResponseEntity<?> removeDirectedEdgeFromPatternView(@PathVariable UUID patternViewId, @PathVariable UUID directedEdgeId) {
        this.patternViewService.removeDirectedEdgeFromPatternView(patternViewId, directedEdgeId);

        return ResponseEntity.noContent().build();
    }

    @Operation(operationId = "addUndirectedEdgeToView", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Add undirected edge to pattern view")
    @PostMapping(value = "/patternViews/{patternViewId}/undirectedEdges")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addUndirectedEdgeToView(@PathVariable UUID patternViewId, @RequestBody AddUndirectedEdgeToViewRequest request) {

        if (request.isNewEdge()) {
            UndirectedEdge undirectedEdge = this.patternViewService.createUndirectedEdgeAndAddToPatternView(patternViewId, request);
            return ResponseEntity.created(linkTo(methodOn(PatternRelationDescriptorController.class)
                    .getUndirectedEdgeOfPatternViewById(patternViewId, undirectedEdge.getId())).toUri()).build();
        } else {
            this.patternViewService.addUndirectedEdgeToPatternView(patternViewId, request.getUndirectedEdgeId());
            return ResponseEntity.created(linkTo(methodOn(PatternRelationDescriptorController.class)
                    .getUndirectedEdgeOfPatternViewById(patternViewId, request.getUndirectedEdgeId())).toUri()).build();
        }
    }

    @Operation(operationId = "getUndirectedEdgesOfView", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Retrieve undirected edges of pattern view")
    @GetMapping(value = "/patternViews/{patternViewId}/undirectedEdges")
    public CollectionModel<EntityModel<UndirectedEdgeModel>> getUndirectedEdgesOfView(@PathVariable UUID patternViewId) {
        List<EntityModel<UndirectedEdgeModel>> undirectedEdgeResources = this.patternViewService.getUndirectedEdgesByPatternViewId(patternViewId)
                .stream()
                .map(UndirectedEdgeModel::from)
                .map(undirectedEdgeModel -> new EntityModel<>(undirectedEdgeModel, getUndirectedEdgeLinksForViewRoute(patternViewId, undirectedEdgeModel.getUndirectedEdge())))
                .collect(Collectors.toList());

        return new CollectionModel<>(undirectedEdgeResources, getUndirectedEdgeCollectionResourceLinksForViewRoute(patternViewId));
    }

    @Operation(operationId = "getUndirectedEdgeOfPatternViewById", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Retrieve undirected edge of pattern view by id")
    @GetMapping(value = "/patternViews/{patternViewId}/undirectedEdges/{undirectedEdgeId}")
    public EntityModel<UndirectedEdgeModel> getUndirectedEdgeOfPatternViewById(@PathVariable UUID patternViewId, @PathVariable UUID undirectedEdgeId) {
        UndirectedEdge undirectedEdge = this.patternViewService.getUndirectedEdgeOfPatternViewById(patternViewId, undirectedEdgeId);

        return new EntityModel<>(UndirectedEdgeModel.from(undirectedEdge), getUndirectedEdgeLinksForViewRoute(patternViewId, undirectedEdge));
    }

    @Operation(operationId = "updateUndirectedEdgeOfPatternView", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Update undirected edge of pattern view by id")
    @PutMapping(value = "/patternViews/{patternViewId}/undirectedEdges/{undirectedEdgeId}")
    public EntityModel<UndirectedEdgeModel> putUndirectedEdgeToPatternView(@PathVariable UUID patternViewId, @PathVariable UUID undirectedEdgeId, @RequestBody UpdateUndirectedEdgeRequest request) {
        request.setUndirectedEdgeId(undirectedEdgeId);
        UndirectedEdge undirectedEdge = this.patternViewService.updateUndirectedEdgeOfPatternView(patternViewId, request);

        return new EntityModel<>(UndirectedEdgeModel.from(undirectedEdge), getUndirectedEdgeLinksForViewRoute(patternViewId, undirectedEdge));
    }

    @Operation(operationId = "removeUndirectedEdgeFromPatternView", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Remove undirected edge of pattern view by id")
    @DeleteMapping(value = "/patternViews/{patternViewId}/undirectedEdges/{undirectedEdgeId}")
    public ResponseEntity<?> removeUndirectedEdgeFromPatternView(@PathVariable UUID patternViewId, @PathVariable UUID undirectedEdgeId) {
        this.patternViewService.removeUndirectedEdgeFromPatternView(patternViewId, undirectedEdgeId);

        return ResponseEntity.noContent().build();
    }
}
