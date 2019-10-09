package com.patternpedia.api.controller;

import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.repositories.PatternLanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;


public class PatternLanguageController {

    @Autowired
    private PatternLanguageRepository patternLanguageRepository;

    @GetMapping
    public Iterable<PatternLanguage> findAll() {
        return this.patternLanguageRepository.findAll();
    }
}
