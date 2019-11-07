package com.patternpedia.api.controller;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.service.PatternService;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/patterns", produces = "application/hal+json")
public class PatternController {

    private PatternService patternService;

    public PatternController(PatternService patternService) {
        this.patternService = patternService;
    }

    @GetMapping(value = "/{patternId}")
    public EntityModel<Pattern> one(@PathVariable UUID patternId) {
        Pattern pattern = this.patternService.getPatternById(patternId);
        // Todo: Add link to patternViews

        return new EntityModel<>(pattern,
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(pattern.getPatternLanguage().getId())).withRel("patternLanguage")
        );
    }

    @GetMapping(value = "/findByUri", params = "uri")
    public EntityModel<Pattern> findByUri(String uri) {
        Pattern pattern = this.patternService.getPatternByUri(uri);
        return new EntityModel<>(pattern,
                linkTo(methodOn(PatternController.class).one(pattern.getId())).withSelfRel(),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(pattern.getPatternLanguage().getId())).withRel("patternLanguage")
        );
    }
}
