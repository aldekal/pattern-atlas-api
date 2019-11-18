package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.UndirectedHyperedge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface UndirectedHyperedgeRepository extends CrudRepository<UndirectedHyperedge, UUID> {
}
