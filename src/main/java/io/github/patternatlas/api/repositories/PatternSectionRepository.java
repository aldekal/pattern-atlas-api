package io.github.patternatlas.api.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.patternatlas.api.entities.PatternSectionSchema;

@RepositoryRestResource(exported = false)
public interface PatternSectionRepository extends CrudRepository<PatternSectionSchema, Long> {
}
