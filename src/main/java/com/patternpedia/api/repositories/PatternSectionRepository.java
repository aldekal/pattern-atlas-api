package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.PatternSectionSchema;
import org.springframework.data.repository.CrudRepository;

public interface PatternSectionRepository extends CrudRepository<PatternSectionSchema, Long> {
}
