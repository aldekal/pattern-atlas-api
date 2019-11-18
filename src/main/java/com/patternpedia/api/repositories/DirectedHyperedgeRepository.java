package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.DirectedHyperedge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface DirectedHyperedgeRepository extends CrudRepository<DirectedHyperedge, UUID> {
}
