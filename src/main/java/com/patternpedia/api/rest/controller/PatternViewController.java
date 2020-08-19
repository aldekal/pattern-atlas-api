package com.patternpedia.api.rest.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.patternpedia.api.entities.PatternView;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patternpedia.api.rest.model.PatternLanguageGraphModel;
import com.patternpedia.api.service.PatternViewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.commons.text.CaseUtils;
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
@RequestMapping(value = "/patternViews", produces = "application/hal+json")
public class PatternViewController {

    private PatternViewService patternViewService;
    private ObjectCodec objectMapper;

    public PatternViewController(PatternViewService patternViewService, ObjectMapper objectMapper) {
        this.patternViewService = patternViewService;
        this.objectMapper = objectMapper;
    }

    private static List<Link> getPatternViewCollectionLinks() {
        List<Link> links = new ArrayList<>();

        try {
            Link findByUriLink = linkTo(methodOn(PatternViewController.class).findPatternViewByUri(null)).withRel("findByUri");
            links.add(findByUriLink);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        links.add(linkTo(methodOn(PatternViewController.class).getAllPatternViews()).withSelfRel()
                .andAffordance(afford(methodOn(PatternViewController.class).createPatternView(null))));
        links.add(linkTo(methodOn(PatternViewController.class).getAllPatternViews()).withRel("patternViews"));
        links.add(linkTo(methodOn(PatternViewController.class).getAllPatternViews()).withRel("graph"));

        return links;
    }

    private static List<Link> getPatternViewLinks(PatternView patternView) {
        List<Link> links = new ArrayList<>();

        links.add(
                linkTo(methodOn(PatternViewController.class).getPatternViewById(patternView.getId())).withSelfRel()
                        .andAffordance(afford(methodOn(PatternViewController.class).putPatternView(patternView.getId(), null)))
                        .andAffordance(afford(methodOn(PatternViewController.class).deletePatternView(patternView.getId())))
        );
        links.add(linkTo(methodOn(PatternViewController.class).getAllPatternViews()).withRel("patternViews"));
        links.add(linkTo(methodOn(PatternController.class).getPatternsOfPatternView(patternView.getId())).withRel("patterns"));
        links.add(linkTo(methodOn(PatternViewController.class).getPatterViewGraph(patternView.getId())).withRel("graph"));
        links.add(linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgesOfView(patternView.getId())).withRel("directedEdges"));
        links.add(linkTo(methodOn(PatternRelationDescriptorController.class).getUndirectedEdgesOfView(patternView.getId())).withRel("undirectedEdges"));

        return links;
    }

    @Operation(operationId = "getPatterViewGraph", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Retrieve pattern view graph")
    @GetMapping(value = "/{patternViewId}/graph")
    ResponseEntity<?> getPatterViewGraph(@PathVariable UUID patternViewId) {
        Object graph = this.patternViewService.getGraphOfPatternView(patternViewId);

        PatternLanguageGraphModel model = new PatternLanguageGraphModel();
        if (null == graph) {
            model.setGraph(this.objectMapper.createArrayNode());
        } else {
            model.setGraph(graph);
        }
        EntityModel<Object> entityModel = new EntityModel<>(model, linkTo(methodOn(PatternViewController.class).getPatternViewById(patternViewId)).withRel("patternLanguage"),
                linkTo(methodOn(PatternViewController.class).getPatterViewGraph(patternViewId)).withSelfRel()
                        .andAffordance(afford(methodOn(PatternViewController.class).postPatternViewGraph(patternViewId, null)))
                        .andAffordance(afford(methodOn(PatternViewController.class).putPatternViewGraph(patternViewId, null)))
                        .andAffordance(afford(methodOn(PatternViewController.class).deletePatternViewGraph(patternViewId))));
        return ResponseEntity.ok(entityModel);
    }

    @Operation(operationId = "getAllPatternViews", responses = {@ApiResponse(responseCode = "200")}, description = "Retrieve all pattern views")
    @GetMapping
    public CollectionModel<EntityModel<PatternView>> getAllPatternViews() {

        List<EntityModel<PatternView>> patternViews = this.patternViewService.getAllPatternViews()
                .stream()
                .map(patternView -> new EntityModel<>(patternView,
                        getPatternViewLinks(patternView)))
                .collect(Collectors.toList());

        return new CollectionModel<>(patternViews, getPatternViewCollectionLinks());
    }

    @Operation(operationId = "createPatternView", responses = {@ApiResponse(responseCode = "201")}, description = "Create a pattern view")
    @PostMapping
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createPatternView(@RequestBody PatternView patternView) {
        String patternViewNameAsCamelCase = CaseUtils.toCamelCase(patternView.getName(), false);
        String uri = String.format("https://patternpedia.org/patternViews/%s", patternViewNameAsCamelCase);
        patternView.setUri(uri);

        PatternView createdPatternView = this.patternViewService.createPatternView(patternView);

        return ResponseEntity.created(linkTo(methodOn(PatternViewController.class)
                .getPatternViewById(createdPatternView.getId())).toUri()).build();
    }

    @Operation(operationId = "getPatternViewById", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Retrieve pattern view by id")
    @GetMapping(value = "/{patternViewId}")
    public EntityModel<PatternView> getPatternViewById(@PathVariable UUID patternViewId) {
        PatternView patternView = this.patternViewService.getPatternViewById(patternViewId);

        return new EntityModel<>(patternView, getPatternViewLinks(patternView));
    }

    @Operation(operationId = "getPatternViewByURI", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Retrieve pattern view by URI")
    @GetMapping(value = "/findByUri")
    public EntityModel<PatternView> findPatternViewByUri(@RequestParam String encodedUri) throws UnsupportedEncodingException {
        String uri = URLDecoder.decode(encodedUri, StandardCharsets.UTF_8.toString());
        PatternView patternView = this.patternViewService.getPatternViewByUri(uri);

        return new EntityModel<>(patternView, getPatternViewLinks(patternView));
    }

    @Operation(operationId = "updatePatternView", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Update pattern view by id")
    @PutMapping(value = "/{patternViewId}")
    public ResponseEntity<?> putPatternView(@PathVariable UUID patternViewId, @RequestBody PatternView patternView) {
        patternView = this.patternViewService.updatePatternView(patternView);

        return ResponseEntity.ok(patternView);
    }

    @Operation(operationId = "getPatternViewById", responses = {@ApiResponse(responseCode = "204"), @ApiResponse(responseCode = "404")}, description = "Delete pattern view by id")
    @DeleteMapping(value = "/{patternViewId}")
    public ResponseEntity<?> deletePatternView(@PathVariable UUID patternViewId) {
        this.patternViewService.deletePatternView(patternViewId);
        return ResponseEntity.noContent().build();
    }

    @Operation(operationId = "createPatternViewGraph", responses = {@ApiResponse(responseCode = "201"), @ApiResponse(responseCode = "404")}, description = "Create pattern view graph")
    @PostMapping(value = "/{patternViewId}/graph")
    ResponseEntity<?> postPatternViewGraph(@PathVariable UUID patternViewId, @RequestBody Object graph) {
        this.patternViewService.updateGraphOfPatternView(patternViewId, graph);
        return ResponseEntity.created(linkTo(methodOn(PatternLanguageController.class).getPatternLanguageGraph(patternViewId)).toUri())
                .build();
    }

    @Operation(operationId = "updatePatternViewGraph", responses = {@ApiResponse(responseCode = "204"), @ApiResponse(responseCode = "404")}, description = "Update pattern view graph")
    @PutMapping(value = "/{patternViewId}/graph")
    ResponseEntity<?> putPatternViewGraph(@PathVariable UUID patternViewId, @RequestBody Object graph) {
        this.patternViewService.updateGraphOfPatternView(patternViewId, graph);
        return ResponseEntity.noContent().build();
    }

    @Operation(operationId = "deletePatternViewGraph", responses = {@ApiResponse(responseCode = "204"), @ApiResponse(responseCode = "404")}, description = "Delete pattern view graph")
    @DeleteMapping(value = "/{patternViewId}/graph")
    ResponseEntity<?> deletePatternViewGraph(@PathVariable UUID patternViewId) {
        this.patternViewService.deletePatternView(patternViewId);
        return ResponseEntity.noContent().build();
    }
}
