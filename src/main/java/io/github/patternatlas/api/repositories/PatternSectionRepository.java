package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.PatternSectionSchema;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface PatternSectionRepository extends CrudRepository<PatternSectionSchema, Long> {
}
