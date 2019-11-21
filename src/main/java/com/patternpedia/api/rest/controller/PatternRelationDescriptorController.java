package com.patternpedia.api.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.PatternView;
import com.patternpedia.api.entities.UndirectedEdge;
import com.patternpedia.api.service.PatternLanguageService;
import com.patternpedia.api.service.PatternViewService;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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
public class PatternRelationDescriptorController {

    private PatternLanguageService patternLanguageService;
    private PatternViewService patternViewService;

    public PatternRelationDescriptorController(PatternLanguageService patternLanguageService,
                                               PatternViewService patternViewService) {
        this.patternLanguageService = patternLanguageService;
        this.patternViewService = patternViewService;
    }

    private static List<Link> getDirectedEdgeLinks(UUID patternViewId, DirectedEdge directedEdge) {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getDirectedEdgeOfPatternViewById(patternViewId, directedEdge.getId())).withSelfRel()
                .andAffordance(afford(methodOn(PatternRelationDescriptorController.class).removeDirectedEdgeFromPatternView(patternViewId, directedEdge.getId())))
                .andAffordance(afford(methodOn(PatternRelationDescriptorController.class).putDirectedEdgeToPatternView(patternViewId, directedEdge.getId(), directedEdge))));

        links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getDirectedEdgesOfView(patternViewId)).withRel("directedEdges"));

        if (null != directedEdge.getPatternViews()) {
            for (PatternView patternView : directedEdge.getPatternViews()) {
                links.add(linkTo(methodOn(PatternViewController.class).getPatternViewById(patternView.getId())).withRel("patternView"));
            }
        }

        if (null != directedEdge.getPatternLanguage()) {
            links.add(linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(directedEdge.getPatternLanguage().getId())).withRel("patternLanguage"));
        }

        return links;
    }

    private static List<Link> getUndirectedEdgeLinks(UUID patternViewId, UndirectedEdge undirectedEdge) {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getUndirectedEdgeOfPatternViewById(patternViewId, undirectedEdge.getId())).withSelfRel()
                .andAffordance(afford(methodOn(PatternRelationDescriptorController.class).removeUndirectedEdgeFromPatternView(patternViewId, undirectedEdge.getId())))
                .andAffordance(afford(methodOn(PatternRelationDescriptorController.class).putUndirectedEdgeToPatternView(patternViewId, undirectedEdge.getId(), undirectedEdge))));

        links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getUndirectedEdgesOfView(patternViewId)).withRel("undirectedEdges"));

        if (null != undirectedEdge.getPatternViews()) {
            for (PatternView patternView : undirectedEdge.getPatternViews()) {
                links.add(linkTo(methodOn(PatternViewController.class).getPatternViewById(patternView.getId())).withRel("patternView"));
            }
        }

        if (null != undirectedEdge.getPatternLanguage()) {
            links.add(linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(undirectedEdge.getPatternLanguage().getId())).withRel("patternLanguage"));
        }

        return links;
    }

    private static List<Link> getDirectedEdgeCollectionResourceLinks(UUID patternViewId) {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getDirectedEdgesOfView(patternViewId)).withSelfRel()
                .andAffordance(afford(methodOn(PatternRelationDescriptorController.class)
                        .createDirectedEdgeAndAddToPatternLanguage(patternViewId, null)))
                .andAffordance(afford(methodOn(PatternRelationDescriptorController.class)
                        .addDirectedEdgeToView(patternViewId, null))));

        links.add(linkTo(methodOn(PatternViewController.class)
                .getPatternViewById(patternViewId)).withRel("patternView"));

        return links;
    }

    private static List<Link> getUndirectedEdgeCollectionResourceLinks(UUID patternViewId) {
        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getUndirectedEdgesOfView(patternViewId)).withSelfRel()
                .andAffordance(afford(methodOn(PatternRelationDescriptorController.class)
                        .createUndirectedEdgeAndAddToPatternLanguage(patternViewId, null)))
                .andAffordance(afford(methodOn(PatternRelationDescriptorController.class)
                        .addUndirectedEdgeToView(patternViewId, null))));

        links.add(linkTo(methodOn(PatternViewController.class)
                .getPatternViewById(patternViewId)).withRel("patternView"));

        return links;
    }

    @GetMapping(value = "/patternLanguages/{patternLanguageId}/directedEdges")
    public CollectionModel<EntityModel<DirectedEdge>> getDirectedEdgesOfPatternLanguage(@PathVariable UUID patternLanguageId) {
        List<EntityModel<DirectedEdge>> directedEdges = this.patternLanguageService.getDirectedEdgesOfPatternLanguage(patternLanguageId)
                .stream()
                .map(directedEdge -> new EntityModel<>(directedEdge,
                        linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgeOfPatternLanguageById(patternLanguageId, directedEdge.getId())).withSelfRel(),
                        linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgesOfPatternLanguage(patternLanguageId)).withRel("directedEdges"),
                        linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(patternLanguageId, directedEdge.getSource().getId())).withRel("source"),
                        linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(patternLanguageId, directedEdge.getTarget().getId())).withRel("target"))
                ).collect(Collectors.toList());
        return new CollectionModel<>(directedEdges,
                linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgesOfPatternLanguage(patternLanguageId)).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage")
        );
    }

    @PostMapping(value = "/patternLanguages/{patternLanguageId}/directedEdges")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createDirectedEdgeAndAddToPatternLanguage(@PathVariable UUID patternLanguageId, @RequestBody DirectedEdge directedEdge) {
        directedEdge = this.patternLanguageService.createDirectedEdgeAndAddToPatternLanguage(patternLanguageId, directedEdge);

        return ResponseEntity
                .created(linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgeOfPatternLanguageById(patternLanguageId, directedEdge.getId())).toUri())
                .body(directedEdge);
    }

    @GetMapping(value = "/patternLanguages/{patternLanguageId}/undirectedEdges")
    public CollectionModel<EntityModel<UndirectedEdge>> getUndirectedEdgesOfPatternLanguage(@PathVariable UUID patternLanguageId) {
        List<EntityModel<UndirectedEdge>> undirectedEdges = this.patternLanguageService.getUndirectedEdgesOfPatternLanguage(patternLanguageId)
                .stream()
                .map(undirectedEdge -> new EntityModel<>(undirectedEdge,
                        linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgeOfPatternLanguageById(patternLanguageId, undirectedEdge.getId())).withSelfRel(),
                        linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgesOfPatternLanguage(patternLanguageId)).withRel("directedEdges"),
                        linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(patternLanguageId, undirectedEdge.getP1().getId())).withRel("pattern1"),
                        linkTo(methodOn(PatternController.class).getPatternOfPatternLanguageById(patternLanguageId, undirectedEdge.getP2().getId())).withRel("pattern2"))
                ).collect(Collectors.toList());
        return new CollectionModel<>(undirectedEdges,
                linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgesOfPatternLanguage(patternLanguageId)).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage")
        );
    }

    @PostMapping(value = "/patternLanguages/{patternLanguageId}/undirectedEdges")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createUndirectedEdgeAndAddToPatternLanguage(@PathVariable UUID patternLanguageId, @RequestBody UndirectedEdge undirectedEdge) {
        undirectedEdge = this.patternLanguageService.createUndirectedEdgeAndAddToPatternLanguage(patternLanguageId, undirectedEdge);

        return ResponseEntity
                .created(linkTo(methodOn(PatternRelationDescriptorController.class).getUndirectedEdgeOfPatternLanguageById(patternLanguageId, undirectedEdge.getId())).toUri())
                .body(undirectedEdge);
    }

    @GetMapping(value = "/patternLanguages/{patternLanguageId}/undirectedEdges/{undirectedEdgeId}")
    public EntityModel<UndirectedEdge> getUndirectedEdgeOfPatternLanguageById(@PathVariable UUID patternLanguageId, @PathVariable UUID undirectedEdgeId) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);

        UndirectedEdge result = patternLanguage.getUndirectedEdges().stream()
                .filter(undirectedEdge -> undirectedEdge.getId().equals(undirectedEdgeId)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format("UndirectedEdge %s not contained in PatternLanguage %s", undirectedEdgeId, patternLanguageId)));
        return new EntityModel<>(result,
                linkTo(methodOn(PatternRelationDescriptorController.class).getUndirectedEdgeOfPatternLanguageById(patternLanguageId, undirectedEdgeId)).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
    }

    @GetMapping(value = "/patternLanguages/{patternLanguageId}/directedEdges/{directedEdgeId}")
    public EntityModel<DirectedEdge> getDirectedEdgeOfPatternLanguageById(@PathVariable UUID patternLanguageId, @PathVariable UUID directedEdgeId) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);

        DirectedEdge result = patternLanguage.getDirectedEdges().stream()
                .filter(directedEdge -> directedEdge.getId().equals(directedEdgeId)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format("DirectedEdge %s not contained in PatternLanguage %s", directedEdgeId, patternLanguageId)));
        return new EntityModel<>(result,
                linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgeOfPatternLanguageById(patternLanguageId, directedEdgeId)).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
    }

    @PostMapping(value = "/patternViews/{patternViewId}/directedEdges")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addDirectedEdgeToView(@PathVariable UUID patternViewId, @RequestBody DirectedEdge directedEdge) {
        if (null != directedEdge.getId()) {
            this.patternViewService.addDirectedEdgeToPatternView(patternViewId, directedEdge.getId());
        } else {
            directedEdge = this.patternViewService.createDirectedEdgeAndAddToPatternView(patternViewId, directedEdge);
        }

        return ResponseEntity.created(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getDirectedEdgeOfPatternViewById(patternViewId, directedEdge.getId())).toUri()).build();
    }

    @GetMapping(value = "/patternViews/{patternViewId}/directedEdges")
    public CollectionModel<EntityModel<DirectedEdge>> getDirectedEdgesOfView(@PathVariable UUID patternViewId) {
        List<EntityModel<DirectedEdge>> directedEdgeResources = this.patternViewService.getDirectedEdgesByPatternViewId(patternViewId)
                .stream()
                .map(directedEdge -> new EntityModel<>(directedEdge, getDirectedEdgeLinks(patternViewId, directedEdge)))
                .collect(Collectors.toList());

        return new CollectionModel<>(directedEdgeResources, getDirectedEdgeCollectionResourceLinks(patternViewId));
    }

    @GetMapping(value = "/patternViews/{patternViewId}/directedEdges/{directedEdgeId}")
    public EntityModel<DirectedEdge> getDirectedEdgeOfPatternViewById(@PathVariable UUID patternViewId, @PathVariable UUID directedEdgeId) {
        DirectedEdge directedEdge = this.patternViewService.getDirectedEdgeOfPatternViewById(patternViewId, directedEdgeId);
        return new EntityModel<>(directedEdge, getDirectedEdgeLinks(patternViewId, directedEdge));
    }

    @PutMapping(value = "/patternViews/{patternViewId}/directedEdges/{directedEdgeId}")
    public EntityModel<DirectedEdge> putDirectedEdgeToPatternView(@PathVariable UUID patternViewId, @PathVariable UUID directedEdgeId, @RequestBody DirectedEdge directedEdge) {
        directedEdge = this.patternViewService.updateDirectedEdgeOfPatternView(patternViewId, directedEdge);
        return new EntityModel<>(directedEdge, getDirectedEdgeLinks(patternViewId, directedEdge));
    }

    @DeleteMapping(value = "/patternViews/{patternViewId}/directedEdges/{directedEdgeId}")
    public ResponseEntity<?> removeDirectedEdgeFromPatternView(@PathVariable UUID patternViewId, @PathVariable UUID directedEdgeId) {
        this.patternViewService.removeDirectedEdgeFromPatternView(patternViewId, directedEdgeId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/patternViews/{patternViewId}/undirectedEdges")
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addUndirectedEdgeToView(@PathVariable UUID patternViewId, @RequestBody UndirectedEdge undirectedEdge) {
        if (null != undirectedEdge.getId()) {
            this.patternViewService.addUndirectedEdgeToPatternView(patternViewId, undirectedEdge.getId());
        } else {
            undirectedEdge = this.patternViewService.createUndirectedEdgeAndAddToPatternView(patternViewId, undirectedEdge);
        }

        return ResponseEntity.created(linkTo(methodOn(PatternRelationDescriptorController.class)
                .getUndirectedEdgeOfPatternViewById(patternViewId, undirectedEdge.getId())).toUri()).build();
    }

    @GetMapping(value = "/patternViews/{patternViewId}/undirectedEdges")
    public CollectionModel<EntityModel<UndirectedEdge>> getUndirectedEdgesOfView(@PathVariable UUID patternViewId) {
        List<EntityModel<UndirectedEdge>> undirectedEdgeResources = this.patternViewService.getUndirectedEdgesByPatternViewId(patternViewId)
                .stream()
                .map(undirectedEdge -> new EntityModel<>(undirectedEdge, getUndirectedEdgeLinks(patternViewId, undirectedEdge)))
                .collect(Collectors.toList());

        return new CollectionModel<>(undirectedEdgeResources, getUndirectedEdgeCollectionResourceLinks(patternViewId));
    }

    @GetMapping(value = "/patternViews/{patternViewId}/undirectedEdges/{undirectedEdgeId}")
    public EntityModel<UndirectedEdge> getUndirectedEdgeOfPatternViewById(@PathVariable UUID patternViewId, @PathVariable UUID undirectedEdgeId) {
        UndirectedEdge undirectedEdge = this.patternViewService.getUndirectedEdgeOfPatternViewById(patternViewId, undirectedEdgeId);
        return new EntityModel<>(undirectedEdge, getUndirectedEdgeLinks(patternViewId, undirectedEdge));
    }

    @PutMapping(value = "/patternViews/{patternViewId}/undirectedEdges/{undirectedEdgeId}")
    public EntityModel<UndirectedEdge> putUndirectedEdgeToPatternView(@PathVariable UUID patternViewId, @PathVariable UUID undirectedEdgeId, @RequestBody UndirectedEdge undirectedEdge) {
        undirectedEdge = this.patternViewService.updateUndirectedEdgeOfPatternView(patternViewId, undirectedEdge);
        return new EntityModel<>(undirectedEdge, getUndirectedEdgeLinks(patternViewId, undirectedEdge));
    }

    @DeleteMapping(value = "/patternViews/{patternViewId}/undirectedEdges/{undirectedEdgeId}")
    public ResponseEntity<?> removeUndirectedEdgeFromPatternView(@PathVariable UUID patternViewId, @PathVariable UUID undirectedEdgeId) {
        this.patternViewService.removeUndirectedEdgeFromPatternView(patternViewId, undirectedEdgeId);
        return ResponseEntity.noContent().build();
    }
}
