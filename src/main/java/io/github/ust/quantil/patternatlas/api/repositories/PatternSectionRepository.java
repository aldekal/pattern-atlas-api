package io.github.ust.quantil.patternatlas.api.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.ust.quantil.patternatlas.api.entities.PatternSectionSchema;

@RepositoryRestResource(exported = false)
public interface PatternSectionRepository extends CrudRepository<PatternSectionSchema, Long> {
}
