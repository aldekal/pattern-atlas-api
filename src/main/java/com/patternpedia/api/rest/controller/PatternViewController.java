package com.patternpedia.api.rest.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.patternpedia.api.entities.PatternView;
import com.patternpedia.api.service.PatternViewService;

import org.apache.commons.text.CaseUtils;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "/patternViews", produces = "application/hal+json")
public class PatternViewController {

    private PatternViewService patternViewService;

    public PatternViewController(PatternViewService patternViewService) {
        this.patternViewService = patternViewService;
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
        links.add(linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgesOfView(patternView.getId())).withRel("directedEdges"));
        links.add(linkTo(methodOn(PatternRelationDescriptorController.class).getUndirectedEdgesOfView(patternView.getId())).withRel("undirectedEdges"));

        return links;
    }

    @GetMapping
    public CollectionModel<EntityModel<PatternView>> getAllPatternViews() {

        // Todo: This is a hack. How can we influence serialization to prevent embedding content of patterns (--> master assembler)
        List<PatternView> preparedViews = this.patternViewService.getAllPatternViews();
        for (PatternView patternView : preparedViews) {
            patternView.setPatterns(PatternController.removeContentFromPatterns(patternView.getPatterns()));
        }

        List<EntityModel<PatternView>> patternViews = preparedViews
                .stream()
                .map(patternView -> new EntityModel<>(patternView,
                        getPatternViewLinks(patternView)))
                .collect(Collectors.toList());

        return new CollectionModel<>(patternViews, getPatternViewCollectionLinks());
    }

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

    @GetMapping(value = "/{patternViewId}")
    public EntityModel<PatternView> getPatternViewById(@PathVariable UUID patternViewId) {
        PatternView patternView = this.patternViewService.getPatternViewById(patternViewId);

        // Todo: This is a hack. How can we influence serialization to prevent embedding content of patterns (--> master assembler)
        if (null != patternView.getPatterns()) {
            patternView.setPatterns(PatternController.removeContentFromPatterns(patternView.getPatterns()));
        }

        return new EntityModel<>(patternView, getPatternViewLinks(patternView));
    }

    @GetMapping(value = "/findByUri")
    public EntityModel<PatternView> findPatternViewByUri(@RequestParam String encodedUri) throws UnsupportedEncodingException {
        String uri = URLDecoder.decode(encodedUri, StandardCharsets.UTF_8.toString());
        PatternView patternView = this.patternViewService.getPatternViewByUri(uri);

        // Todo: This is a hack. How can we influence serialization to prevent embedding content of patterns (--> master assembler)
        if (null != patternView.getPatterns()) {
            patternView.setPatterns(PatternController.removeContentFromPatterns(patternView.getPatterns()));
        }

        return new EntityModel<>(patternView, getPatternViewLinks(patternView));
    }

    @PutMapping(value = "/{patternViewId}")
    public ResponseEntity<?> putPatternView(@PathVariable UUID patternViewId, @RequestBody PatternView patternView) {
        patternView = this.patternViewService.updatePatternView(patternView);

        // Todo: This is a hack. How can we influence serialization to prevent embedding content of patterns (--> master assembler)
        if (null != patternView.getPatterns()) {
            patternView.setPatterns(PatternController.removeContentFromPatterns(patternView.getPatterns()));
        }

        return ResponseEntity.ok(patternView);
    }

    @DeleteMapping(value = "/{patternViewId}")
    public ResponseEntity<?> deletePatternView(@PathVariable UUID patternViewId) {
        this.patternViewService.deletePatternView(patternViewId);
        return ResponseEntity.noContent().build();
    }
}
