package com.patternpedia.api.rest.controller;

import com.patternpedia.api.entities.PatternView;
import com.patternpedia.api.service.PatternViewService;
import org.apache.commons.text.CaseUtils;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "/patternViews", produces = "application/hal+json")
public class PatternViewController {

    private PatternViewService patternViewService;

    public PatternViewController(PatternViewService patternViewService) {
        this.patternViewService = patternViewService;
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
        String uri = String.format("https://patternpedia.org/patternLanguages/%s", patternViewNameAsCamelCase);
        patternView.setUri(uri);

        PatternView createdPatternView = this.patternViewService.createPatternView(patternView);
        return ResponseEntity
                .created(linkTo(methodOn(PatternViewController.class).getPatternViewById(createdPatternView.getId())).toUri())
                .build();
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

    @PatchMapping(value = "/{patternViewId}")
    public ResponseEntity<?> patchPatternView(@PathVariable UUID patternViewId, @RequestBody PatternView patternView) {
        return this.putPatternView(patternViewId, patternView);
    }

    @DeleteMapping(value = "/{patternViewId}")
    public ResponseEntity<?> deletePatternView(@PathVariable UUID patternViewId) {
        this.patternViewService.deletePatternView(patternViewId);
        return ResponseEntity.ok().build();
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
                        .andAffordance(afford(methodOn(PatternViewController.class).patchPatternView(patternView.getId(), null)))
                        .andAffordance(afford(methodOn(PatternViewController.class).deletePatternView(patternView.getId())))
        );
        links.add(linkTo(methodOn(PatternViewController.class).getAllPatternViews()).withRel("patternViews"));
        links.add(linkTo(methodOn(PatternController.class).getAllPatternsOfPatternView(patternView.getId())).withRel("patterns"));

        return links;
    }
}
