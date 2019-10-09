package com.patternpedia.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PatternLanguageRepository extends CrudRepository<com.patternpedia.demo.entities.PatternLanguage, UUID> {
}
