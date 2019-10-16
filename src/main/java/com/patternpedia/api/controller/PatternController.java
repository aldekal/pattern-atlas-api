package com.patternpedia.api.controller;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.repositories.PatternRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.UUID;

@EnableWebMvc
@RestController
@RequestMapping(value = "/patterns", produces = "application/hal+json")
public class PatternController {

    private PatternRepository patternRepository;

    public PatternController(PatternRepository patternRepository) {
        this.patternRepository = patternRepository;
    }

    @GetMapping(value = "/{patternId}")
    public Resource<Pattern> one(@PathVariable UUID patternId) {
        Pattern pattern = this.patternRepository.findById(patternId)
                .orElseThrow(() -> new ResourceNotFoundException("Pattern not found: " + patternId));
        // Todo: Add link to patternViews
        return new Resource<>(pattern);
        // linkTo(methodOn(PatternLanguageController.class).one(pattern.getPatternLanguage().getId())).withRel("patternLanguage"));
    }
}
