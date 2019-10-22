package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.PatternSchema;
import org.springframework.data.repository.CrudRepository;

public interface PatternSchemaRepository extends CrudRepository<PatternSchema, Long> {
}
