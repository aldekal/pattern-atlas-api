package com.patternpedia.api.repositories;

import java.util.UUID;

import com.patternpedia.api.entities.edge.DirectedHyperedge;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface DirectedHyperedgeRepository extends CrudRepository<DirectedHyperedge, UUID> {
}
