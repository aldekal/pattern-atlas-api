package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.PatternSectionSchema;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(exported = false)
public interface PatternSectionSchemaRepository extends CrudRepository<PatternSectionSchema, Long> {
}
