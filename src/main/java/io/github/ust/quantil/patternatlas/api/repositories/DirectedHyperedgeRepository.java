package io.github.ust.quantil.patternatlas.api.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.ust.quantil.patternatlas.api.entities.DirectedHyperedge;

@RepositoryRestResource(exported = false)
public interface DirectedHyperedgeRepository extends CrudRepository<DirectedHyperedge, UUID> {
}
