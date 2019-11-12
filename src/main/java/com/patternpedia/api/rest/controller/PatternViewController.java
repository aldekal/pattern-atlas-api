package com.patternpedia.api.rest.controller;

import com.patternpedia.api.entities.PatternView;
import com.patternpedia.api.service.PatternViewService;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @GetMapping
    public CollectionModel<EntityModel<PatternView>> getAllPatternViews() {
        List<EntityModel<PatternView>> patternViews = this.patternViewService.getAllPatternViews()
                .stream()
                .map(patternView -> new EntityModel<>(patternView,
                        linkTo(methodOn(PatternController.class).getAllPatternsOfPatternView(patternView.getId())).withRel("patterns"),
                        linkTo(methodOn(PatternViewController.class).getPatternViewById(patternView.getId())).withSelfRel(),
                        linkTo(methodOn(PatternViewController.class).getAllPatternViews()).withRel("patternViews")))
                .collect(Collectors.toList());

        UriTemplate uriTemplate = UriTemplate.of("/findByUri")
                .with(new TemplateVariable("uri", TemplateVariable.VariableType.REQUEST_PARAM));
        Link findByUriLink = null;
        try {
            findByUriLink = linkTo(methodOn(PatternViewController.class).findPatternViewByUri(null)).withRel("findByUri");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new CollectionModel<>(patternViews,
                findByUriLink,
                linkTo(methodOn(PatternViewController.class).getAllPatternViews()).withSelfRel());
    }

    @GetMapping(value = "/{patternViewId}")
    public EntityModel<PatternView> getPatternViewById(@PathVariable UUID patternViewId) {
        PatternView patternView = this.patternViewService.getPatternViewById(patternViewId);

        return new EntityModel<>(patternView,
                linkTo(methodOn(PatternViewController.class).getPatternViewById(patternViewId)).withSelfRel(),
                linkTo(methodOn(PatternViewController.class).getAllPatternViews()).withRel("patternViews"));
    }

    @GetMapping(value = "/findByUri")
    public EntityModel<PatternView> findPatternViewByUri(@RequestParam String encodedUri) throws UnsupportedEncodingException {
        String uri = URLDecoder.decode(encodedUri, StandardCharsets.UTF_8.toString());

        PatternView patternView = this.patternViewService.getPatternViewByUri(uri);

        return new EntityModel<>(patternView,
                linkTo(methodOn(PatternViewController.class).getPatternViewById(patternView.getId())).withSelfRel(),
                linkTo(methodOn(PatternController.class).getAllPatternsOfPatternView(patternView.getId())).withRel("patterns"),
                linkTo(methodOn(PatternViewController.class).getAllPatternViews()).withRel("patternViews"));
    }
}
