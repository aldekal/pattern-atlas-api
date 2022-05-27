package io.github.patternatlas.api.rest.controller;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.text.CaseUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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

import io.github.patternatlas.api.rest.model.EdgeDTO;
import io.github.patternatlas.api.rest.model.FileDTO;
import io.github.patternatlas.api.rest.model.PositionDTO;
import io.github.patternatlas.api.service.ConcreteSolutionService;
import io.github.patternatlas.api.service.DesignModelService;
import io.github.patternatlas.api.entities.Pattern;
import io.github.patternatlas.api.entities.designmodel.ConcreteSolution;
import io.github.patternatlas.api.entities.designmodel.DesignModel;
import io.github.patternatlas.api.entities.designmodel.DesignModelEdgeType;
import io.github.patternatlas.api.entities.designmodel.DesignModelPatternEdge;
import io.github.patternatlas.api.entities.designmodel.DesignModelPatternInstance;
import io.github.patternatlas.api.rest.model.PatternInstanceDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.apachecommons.CommonsLog;

@RestController
@ConditionalOnExpression(value = "false")  // TODO: set to true if the design models should be used again
@CommonsLog
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "/design-models", produces = "application/hal+json")
public class DesignModelController {

    private final DesignModelService designModelService;
    private final ConcreteSolutionService concreteSolutionService;

    public DesignModelController(DesignModelService designModelService, ConcreteSolutionService concreteSolutionService) {
        this.designModelService = designModelService;
        this.concreteSolutionService = concreteSolutionService;
    }

    private static List<Link> getDesignModelCollectionLinks() {
        return Arrays.asList(
                linkTo(methodOn(DesignModelController.class).getDesignModels()).withSelfRel()
                        .andAffordance(afford(methodOn(DesignModelController.class).createDesignModel(null))),
                linkTo(methodOn(DesignModelController.class).getDesignModel(null)).withRel("designModel"),
                linkTo(methodOn(DesignModelController.class).getDesignModelPatternEdgeTypes()).withRel("edgeTypes")
        );
    }

    private static List<Link> getDesignModelLinks(UUID designModelId, String selfRel) {
        Map<String, WebMvcLinkBuilder> linkMap = new HashMap<>();

        linkMap.put("designModels", linkTo(methodOn(DesignModelController.class).getDesignModels()));
        linkMap.put("designModel", linkTo(methodOn(DesignModelController.class).getDesignModel(designModelId)));
        linkMap.put("patterns", linkTo(methodOn(DesignModelController.class).getDesignModelPatternInstances(designModelId)));
        linkMap.put("edges", linkTo(methodOn(DesignModelController.class).getDesignModelPatternEdges(designModelId)));
        linkMap.put("edgeTypes", linkTo(methodOn(DesignModelController.class).getDesignModelPatternEdgeTypes()));
        linkMap.put("concreteSolutions", linkTo(methodOn(DesignModelController.class).checkConcreteSolutions(designModelId)));
        linkMap.put("aggregate", linkTo(methodOn(DesignModelController.class).aggregateConcreteSolutions(designModelId, null)));

        List<Link> linkList = new ArrayList<>();
        if (linkMap.containsKey(selfRel)) {
            linkList.add(linkMap.get(selfRel).withSelfRel());
        } else {
            log.error("_self link for " + selfRel + " not found in linkMap");
        }
        for (Map.Entry<String, WebMvcLinkBuilder> linkPair : linkMap.entrySet()) {
            linkList.add(linkPair.getValue().withRel(linkPair.getKey()));
        }

        return linkList;
    }

    @GetMapping("")
    public CollectionModel<EntityModel<DesignModel>> getDesignModels() {

        List<EntityModel<DesignModel>> designModels = this.designModelService.getAllDesignModels()
                .stream()
                .map(designModel -> new EntityModel<>(designModel, getDesignModelLinks(designModel.getId(), "designModel")))
                .collect(toList());

        return new CollectionModel<>(designModels, getDesignModelCollectionLinks());
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

    @GetMapping("/edge-types")
    public EntityModel<Map<String, List<String>>> getDesignModelPatternEdgeTypes() {

        List<String> edgeTypes = this.designModelService.getDesignModelEdgeTypes().stream()
                .map(DesignModelEdgeType::getName)
                .collect(toList());

        return new EntityModel<>(Collections.singletonMap("edgeTypes", edgeTypes), getDesignModelLinks(null, "edgeTypes"));
    }

    @GetMapping("/{designModelId}")
    public EntityModel<DesignModel> getDesignModel(@PathVariable UUID designModelId) {
        DesignModel designModel = this.designModelService.getDesignModel(designModelId);

        return new EntityModel<>(designModel, getDesignModelLinks(designModel.getId(), "designModel"));
    }

    @GetMapping("/{designModelId}/patterns")
    public CollectionModel<EntityModel<PatternInstanceDTO>> getDesignModelPatternInstances(@PathVariable UUID designModelId) {
        List<DesignModelPatternInstance> patternInstances = this.designModelService.getDesignModel(designModelId).getPatterns();

        List<EntityModel<PatternInstanceDTO>> patterns = patternInstances.stream()
                .map(PatternInstanceDTO::from)
                .map(patternModel -> new EntityModel<>(patternModel))
                .collect(toList());

        return new CollectionModel<>(patterns, getDesignModelLinks(designModelId, "patterns"));
    }

    @PostMapping("/{designModelId}/patterns")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addDesignModelPatternInstance(@PathVariable UUID designModelId, @RequestBody Pattern pattern) {

        this.designModelService.addPatternInstance(designModelId, pattern.getId());

        return ResponseEntity.created(linkTo(methodOn(DesignModelController.class).getDesignModelPatternInstances(designModelId)).toUri()).build();
    }

    @PutMapping("/{designModelId}/patterns/{patternInstanceId}/position")
    public ResponseEntity<?> putDesignModelPatternInstancePosition(@PathVariable UUID designModelId, @PathVariable UUID patternInstanceId, @RequestBody PositionDTO position) {

        this.designModelService.updatePatternInstancePosition(designModelId, patternInstanceId, position.getX(), position.getY());

        return ResponseEntity.created(linkTo(methodOn(DesignModelController.class).getDesignModelPatternInstances(designModelId)).toUri()).build();
    }

    @DeleteMapping("/{designModelId}/patterns/{patternInstanceId}")
    public ResponseEntity<?> deleteDesignModelPatternInstance(@PathVariable UUID designModelId, @PathVariable UUID patternInstanceId) {

        this.designModelService.deletePatternInstance(designModelId, patternInstanceId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{designModelId}/edges")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addDesignModelEdge(@PathVariable UUID designModelId, @RequestBody EdgeDTO edgeDTO) {

        this.designModelService.addEdge(designModelId, edgeDTO.getFirstPatternId(), edgeDTO.getSecondPatternId(),
                edgeDTO.isDirectedEdge(), edgeDTO.getType(), edgeDTO.getDescription());

        return ResponseEntity.created(linkTo(methodOn(DesignModelController.class)
                .getDesignModelPatternEdges(designModelId)).toUri()).build();
    }

    @GetMapping("/{designModelId}/edges")
    public CollectionModel<EntityModel<EdgeDTO>> getDesignModelPatternEdges(@PathVariable UUID designModelId) {

        List<DesignModelPatternEdge> designModelPatternEdges = this.designModelService.getEdges(designModelId);

        List<EntityModel<EdgeDTO>> edges = designModelPatternEdges.parallelStream()
                .map(EdgeDTO::from)
                .map(edgeDTO -> new EntityModel<>(edgeDTO))
                .collect(toList());

        return new CollectionModel<>(edges, getDesignModelLinks(designModelId, "edges"));
    }

    @DeleteMapping("/{designModelId}/edges/{sourceId}/{targetId}")
    public ResponseEntity<?> getDesignModelPatternEdges(@PathVariable UUID designModelId, @PathVariable UUID sourceId, @PathVariable UUID targetId) {

        this.designModelService.deleteEdge(designModelId, sourceId, targetId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{designModelId}/concrete-solutions")
    public CollectionModel<ConcreteSolution> checkConcreteSolutions(@PathVariable UUID designModelId) {
        List<DesignModelPatternInstance> patternInstanceList = this.designModelService.getDesignModel(designModelId).getPatterns();
        Set<String> patternUris = patternInstanceList.stream().map(patternInstance -> patternInstance.getPattern().getUri()).collect(Collectors.toSet());
        Set<ConcreteSolution> concreteSolutionSet = new HashSet<>();

        for (String uri : patternUris) {
            this.concreteSolutionService.getConcreteSolutions(URI.create(uri)).forEach(concreteSolution -> concreteSolutionSet.add(concreteSolution));
        }

        return new CollectionModel<>(concreteSolutionSet, getDesignModelLinks(designModelId, "concreteSolutions"));
    }

    @PostMapping("/{designModelId}/aggregate")
    public List<FileDTO> aggregateConcreteSolutions(@PathVariable UUID designModelId, @RequestBody Map<UUID, UUID> patternConcreteSolutionMap) {

        DesignModel designModel = this.designModelService.getDesignModel(designModelId);
        List<DesignModelPatternInstance> patternInstanceList = designModel.getPatterns();
        List<DesignModelPatternEdge> directedEdgeList = designModel.getDirectedEdges();

        return this.concreteSolutionService.aggregate(patternInstanceList, directedEdgeList, patternConcreteSolutionMap);
    }

    @Operation(operationId = "deleteDesignModelById", responses = {@ApiResponse(responseCode = "204"), @ApiResponse(responseCode = "404", content = @Content)}, description = "Delete DesignModel by id")
    @DeleteMapping(value = "/{designModelId}")
    public ResponseEntity<?> deleteDesignModel(@PathVariable UUID designModelId) {
        this.designModelService.deleteDesignModel(designModelId);
        return ResponseEntity.noContent().build();
    }
}
