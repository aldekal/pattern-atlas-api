package com.patternatlas.api.repositories;

import java.util.UUID;

import com.patternatlas.api.entities.PatternSchema;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface PatternSchemaRepository extends CrudRepository<PatternSchema, UUID> {
}
