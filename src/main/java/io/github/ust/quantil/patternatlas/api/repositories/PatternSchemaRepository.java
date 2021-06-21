package io.github.ust.quantil.patternatlas.api.repositories;

import java.util.UUID;

import io.github.ust.quantil.patternatlas.api.entities.PatternSchema;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface PatternSchemaRepository extends CrudRepository<PatternSchema, UUID> {
}
