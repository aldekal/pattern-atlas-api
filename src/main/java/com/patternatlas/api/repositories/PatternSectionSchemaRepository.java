package com.patternatlas.api.repositories;

import com.patternatlas.api.entities.PatternSectionSchema;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface PatternSectionSchemaRepository extends CrudRepository<PatternSectionSchema, Long> {
}
