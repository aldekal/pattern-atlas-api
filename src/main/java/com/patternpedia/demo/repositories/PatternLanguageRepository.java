package com.patternpedia.demo.repositories;

import com.patternpedia.demo.entities.PatternLanguage;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PatternLanguageRepository extends CrudRepository<PatternLanguage, String> {
}
