package io.github.patternatlas.api.rest.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;

import org.apache.commons.text.CaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.patternatlas.api.entities.DirectedEdge;
import io.github.patternatlas.api.entities.Pattern;
import io.github.patternatlas.api.entities.PatternLanguage;
import io.github.patternatlas.api.entities.PatternViewPattern;
import io.github.patternatlas.api.entities.UndirectedEdge;
import io.github.patternatlas.api.exception.DirectedEdgeNotFoundException;
import io.github.patternatlas.api.exception.UndirectedEdgeNotFoundException;
import io.github.patternatlas.api.rest.model.PatternContentModel;
import io.github.patternatlas.api.rest.model.PatternModel;
import io.github.patternatlas.api.rest.model.PatternRenderedContentModel;
import io.github.patternatlas.api.service.PatternLanguageService;
import io.github.patternatlas.api.service.PatternRelationDescriptorService;
import io.github.patternatlas.api.service.PatternRenderService;
import io.github.patternatlas.api.service.PatternService;
import io.github.patternatlas.api.service.PatternViewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "/", produces = "application/hal+json")
public class PatternController {

    final private static Logger LOG = LoggerFactory.getLogger(PatternController.class);

    private final PatternService patternService;
    private final PatternLanguageService patternLanguageService;
    private final PatternViewService patternViewService;
    private final PatternRelationDescriptorService patternRelationDescriptorService;
    private final ObjectMapper objectMapper;
    private final PatternRenderService patternRenderService;

    public PatternController(PatternService patternService,
                             PatternLanguageService patternLanguageService,
                             PatternViewService patternViewService,
                             PatternRelationDescriptorService patternRelationDescriptorService,
                             PatternRenderService patternRenderService,
                             ObjectMapper objectMapper) {
        this.patternService = patternService;
        this.patternLanguageService = patternLanguageService;
        this.patternViewService = patternViewService;
        this.patternRelationDescriptorService = patternRelationDescriptorService;
        this.objectMapper = objectMapper;
        this.patternRenderService = patternRenderService;
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
        links.add(linkTo(methodOn(PatternController.class).getPatternRenderedContentOfPattern(pattern.getPatternLanguage().getId(), pattern.getId())).withRel("renderedContent"));
        links.add(linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(pattern.getPatternLanguage().getId())).withRel("patternLanguage"));

        if (null != pattern.getPatternViews()) {
            for (PatternViewPattern patternViewPattern : pattern.getPatternViews()) {
                links.add(linkTo(methodOn(PatternViewController.class).getPatternViewById(patternViewPattern.getPatternView().getId())).withRel("patternView"));
            }
        }

        return links;
    }

    EdgeResult getPatternLinksForPatternLanguageRoute(Pattern pattern, UUID patternLanguageId) {

        List<DirectedEdge> outgoingEdges;

        try {
            outgoingEdges = this.patternRelationDescriptorService.findDirectedEdgeBySource(pattern);
        } catch (DirectedEdgeNotFoundException ex) {
            outgoingEdges = Collections.emptyList();
        }

        List<DirectedEdge> ingoingEdges;
        try {
            ingoingEdges = this.patternRelationDescriptorService.findDirectedEdgeByTarget(pattern);
        } catch (DirectedEdgeNotFoundException ex) {
            ingoingEdges = Collections.emptyList();
        }

        List<UndirectedEdge> undirectedEdges;
        try {
            undirectedEdges = this.patternRelationDescriptorService.findUndirectedEdgeByPattern(pattern);
        } catch (UndirectedEdgeNotFoundException ex) {
            undirectedEdges = Collections.emptyList();
        }

        return new EdgeResult(outgoingEdges, ingoingEdges, undirectedEdges, this.getPatternLinks(pattern));
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

    @Operation(operationId = "getPatternByURI", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Retrieve patterns by pattern uri")
    @GetMapping(value = "/patterns/search/findByUri")
    EntityModel<Pattern> getPatternByUri(@RequestParam String encodedUri) throws UnsupportedEncodingException {
        String uri = URLDecoder.decode(encodedUri, StandardCharsets.UTF_8.toString());
        Pattern pattern = this.patternService.getPatternByUri(uri);
        return new EntityModel<>(pattern, getPatternLinks(pattern));
    }

    @Operation(operationId = "getPatternsOfPatternLanguage", responses = {@ApiResponse(responseCode = "200")}, description = "Retrieve patterns by pattern language id")
    @GetMapping(value = "/patternLanguages/{patternLanguageId}/patterns")
    CollectionModel<EntityModel<PatternModel>> getPatternsOfPatternLanguage(@PathVariable UUID patternLanguageId) {
        List<EntityModel<PatternModel>> patterns = this.patternLanguageService.getPatternsOfPatternLanguage(patternLanguageId).stream()
                .map(pattern -> new EntityModel<>(PatternModel.from(pattern),
                        linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(patternLanguageId, pattern.getId())).withSelfRel(),
                        linkTo(methodOn(PatternController.class).getPatternContentOfPattern(patternLanguageId, pattern.getId())).withRel("content"),
                        linkTo(methodOn(PatternController.class).getPatternRenderedContentOfPattern(patternLanguageId, pattern.getId())).withRel("renderedContent"),
                        linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage")))
                .collect(Collectors.toList());
        return new CollectionModel<>(patterns, getPatternLanguagePatternCollectionLinks(patternLanguageId));
    }

    private List<Link> getLinksForPattern(EdgeResult patternEdgeResult, UUID patternLanguageId) {

        List<Link> links = new ArrayList<Link>();

        for (UndirectedEdge undirectedEdge : patternEdgeResult.getUndirectedEdges()) {
            if (null != undirectedEdge.getPatternLanguage() && undirectedEdge.getPatternLanguage().getId().equals(patternLanguageId)) {
                // edge is part of pattern language, thus reference the route to edge in pattern language
                links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                        .getUndirectedEdgeOfPatternLanguageById(undirectedEdge.getPatternLanguage().getId(), undirectedEdge.getId())).withRel("undirectedEdges"));
            }
        }

        for (DirectedEdge directedEdge : patternEdgeResult.getIngoingEdges()) {
            if (null != directedEdge.getPatternLanguage() && directedEdge.getPatternLanguage().getId().equals(patternLanguageId)) {
                // edge is part of pattern language, thus reference the route to edge in pattern language
                links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                        .getDirectedEdgeOfPatternLanguageById(directedEdge.getPatternLanguage().getId(), directedEdge.getId())).withRel("ingoingDirectedEdges"));
            }
        }

        for (DirectedEdge directedEdge : patternEdgeResult.getOutgoingEdges()) {
            if (null != directedEdge.getPatternLanguage() && directedEdge.getPatternLanguage().getId().equals(patternLanguageId)) {
                // edge is part of pattern language, thus reference the route to edge in pattern language
                links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                        .getDirectedEdgeOfPatternLanguageById(directedEdge.getPatternLanguage().getId(), directedEdge.getId())).withRel("outgoingDirectedEdges"));
            }
        }

        links.addAll(patternEdgeResult.getStandardPatternLinks());

        return links;
    }

    @Operation(operationId = "getPatternsOfPatternView", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404")}, description = "Retrieve patterns by pattern view id")
    @GetMapping(value = "/patternViews/{patternViewId}/patterns")
    CollectionModel<EntityModel<PatternModel>> getPatternsOfPatternView(@PathVariable UUID patternViewId) {
        List<EntityModel<PatternModel>> patterns = this.patternViewService.getPatternsOfPatternView(patternViewId).stream()
                .map(PatternModel::from)
                .map(patternModel -> new EntityModel<>(patternModel, getPatternLinksForPatternViewRoute(patternModel.getPattern(), patternViewId)))
                .collect(Collectors.toList());
        return new CollectionModel<>(patterns, getPatternViewPatternCollectionLinks(patternViewId));
    }

    @Operation(operationId = "addPatternToPatternView", responses = {@ApiResponse(responseCode = "201")}, description = "add pattern to pattern view")
    @PostMapping(value = "/patternViews/{patternViewId}/patterns")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addPatternToPatternView(@PathVariable UUID patternViewId, @RequestBody Pattern pattern) {
        Object renderedContent = patternRenderService.renderContent(pattern, null);
        if (renderedContent != null) {
            pattern.setRenderedContent(renderedContent);
        } else {
            pattern.setRenderedContent(pattern.getContent());
        }
        this.patternViewService.addPatternToPatternView(patternViewId, pattern.getId());
        return ResponseEntity.created(linkTo(methodOn(PatternController.class)
                .getPatternOfPatternViewById(patternViewId, pattern.getId())).toUri()).build();
    }

    @Operation(operationId = "getPatternOfPatternViewById", responses = {@ApiResponse(responseCode = "200")}, description = "Retrieve single pattern by pattern view id")
    @CrossOrigin(exposedHeaders = "Location")
    @GetMapping(value = "/patternViews/{patternViewId}/patterns/{patternId}")
    EntityModel<Pattern> getPatternOfPatternViewById(@PathVariable UUID patternViewId, @PathVariable UUID patternId) {
        Pattern pattern = this.patternViewService.getPatternOfPatternViewById(patternViewId, patternId);

        return new EntityModel<>(pattern, getPatternLinksForPatternViewRoute(pattern, patternViewId));
    }

    @Operation(operationId = "removePatternFromView", responses = {@ApiResponse(responseCode = "204")}, description = "Delete pattern from pattern view")
    @DeleteMapping(value = "/patternViews/{patternViewId}/patterns/{patternId}")
    ResponseEntity<?> removePatternFromView(@PathVariable UUID patternViewId, @PathVariable UUID patternId) {
        this.patternViewService.removePatternFromPatternView(patternViewId, patternId);
        return ResponseEntity.noContent().build();
    }

    @Operation(operationId = "addPatternToPatternLanguage", responses = {@ApiResponse(responseCode = "201")}, description = "Add pattern to pattern language")
    @PostMapping(value = "/patternLanguages/{patternLanguageId}/patterns")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?> addPatternToPatternLanguage(@PathVariable UUID patternLanguageId, @Valid @RequestBody Pattern pattern) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);
        if (null == pattern.getUri()) {
            pattern.setUri(patternLanguage.getUri() + '/' + CaseUtils.toCamelCase(pattern.getName(), false));
        }
        Object renderedContent = patternRenderService.renderContent(pattern, null);
        if (renderedContent != null) {
            pattern.setRenderedContent(renderedContent);
        } else {
            pattern.setRenderedContent(pattern.getContent());
        }
        pattern = this.patternLanguageService.createPatternAndAddToPatternLanguage(patternLanguageId, pattern);

        return ResponseEntity.created(linkTo(methodOn(PatternController.class)
                .getPatternOfPatternLanguageById(patternLanguageId, pattern.getId())).toUri()).build();
    }

    @Operation(operationId = "getPatternOfPatternLanguageById", responses = {@ApiResponse(responseCode = "200")}, description = "Retrieve single pattern by pattern language id")
    @GetMapping(value = "/patternLanguages/{patternLanguageId}/patterns/{patternId}")
    EntityModel<Pattern> getPatternOfPatternLanguageById(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId) {
        Pattern pattern = this.patternLanguageService.getPatternOfPatternLanguageById(patternLanguageId, patternId);
        EdgeResult patternLinksForPatternLanguageRoute = getPatternLinksForPatternLanguageRoute(pattern, patternLanguageId);
        List<Link> patternLinks = getLinksForPattern(patternLinksForPatternLanguageRoute, patternLanguageId);
        return new EntityModel<>(pattern, patternLinks);
    }

    @Operation(operationId = "updatePatternByPatternLanguageId", responses = {@ApiResponse(responseCode = "200")}, description = "Update pattern by pattern language id")
    @PutMapping(value = "/patternLanguages/{patternLanguageId}/patterns/{patternId}")
    EntityModel<Pattern> updatePatternViaPut(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId, @Valid @RequestBody Pattern pattern) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);
        Pattern persistedVersion = this.patternService.getPatternById(patternId);
        // Remark: At the moment we do not support changing name, uri of a pattern

        Object renderedContent = patternRenderService.renderContent(pattern, persistedVersion);
        if (renderedContent != null) {
            persistedVersion.setRenderedContent(renderedContent);
        } else {
            persistedVersion.setRenderedContent(pattern.getContent());
        }
        persistedVersion.setIconUrl(pattern.getIconUrl());
        persistedVersion.setPaperRef(pattern.getPaperRef());
        persistedVersion.setContent(pattern.getContent());
        persistedVersion.setName(pattern.getName());

        pattern = this.patternService.updatePattern(persistedVersion);
        return new EntityModel<>(pattern,
                linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(patternLanguageId, patternId)).withSelfRel(),
                linkTo(methodOn(PatternController.class).getPatternContentOfPattern(patternLanguageId, patternId)).withRel("content"),
                linkTo(methodOn(PatternController.class).getPatternRenderedContentOfPattern(patternLanguageId, patternId)).withRel("renderedContent"),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
    }

    @Operation(operationId = "deletePatternOfPatternLanguage", responses = {@ApiResponse(responseCode = "204")}, description = "Delete pattern of pattern language")
    @DeleteMapping(value = "/patternLanguages/{patternLanguageId}/patterns/{patternId}")
    ResponseEntity<?> deletePatternOfPatternLanguage(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId) {
        this.patternLanguageService.deletePatternOfPatternLanguage(patternLanguageId, patternId);
        return ResponseEntity.noContent().build();
    }

    @Operation(operationId = "getPatternContent", responses = {@ApiResponse(responseCode = "200")}, description = "Get content of pattern")
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
                linkTo(methodOn(PatternController.class).getPatternRenderedContentOfPattern(patternLanguageId, patternId)).withRel("renderedContent"),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
    }

    @GetMapping(value = "/patternLanguages/{patternLanguageId}/patterns/{patternId}/renderedContent")
    EntityModel<Object> getPatternRenderedContentOfPattern(@PathVariable UUID patternLanguageId, @PathVariable UUID patternId) {

        Pattern pattern = this.patternLanguageService.getPatternOfPatternLanguageById(patternLanguageId, patternId);
        PatternRenderedContentModel model = new PatternRenderedContentModel();

        if (null == pattern.getRenderedContent()) {
            model.setRenderedContent(this.objectMapper.createObjectNode());
        } else {
            model.setRenderedContent(pattern.getRenderedContent());
        }

        return new EntityModel<>(model,
                linkTo(methodOn(PatternController.class).getPatternContentOfPattern(patternLanguageId, patternId)).withSelfRel(),
                linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(patternLanguageId, patternId)).withRel("pattern"),
                linkTo(methodOn(PatternController.class).getPatternRenderedContentOfPattern(patternLanguageId, patternId)).withRel("content"),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
    }
}
