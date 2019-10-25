package com.patternpedia.api.controller;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.service.PatternService;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@EnableWebMvc
@RestController
@RequestMapping(value = "/patterns", produces = "application/hal+json")
public class PatternController {

    private PatternService patternService;

    public PatternController(PatternService patternService) {
        this.patternService = patternService;
    }

    @GetMapping(value = "/{patternId}")
    public Resource<Pattern> one(@PathVariable UUID patternId) {
        Pattern pattern = this.patternService.getPatternById(patternId);
        // Todo: Add link to patternViews

        return new Resource<>(pattern,
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(pattern.getPatternLanguage().getId())).withRel("patternLanguage"));
    }
}
