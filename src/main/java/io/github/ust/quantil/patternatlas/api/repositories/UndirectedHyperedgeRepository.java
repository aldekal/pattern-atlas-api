package io.github.ust.quantil.patternatlas.api.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.ust.quantil.patternatlas.api.entities.UndirectedHyperedge;

@RepositoryRestResource(exported = false)
public interface UndirectedHyperedgeRepository extends CrudRepository<UndirectedHyperedge, UUID> {
}
