package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.PatternSchema;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PatternSchemaRepository extends CrudRepository<PatternSchema, UUID> {
}
