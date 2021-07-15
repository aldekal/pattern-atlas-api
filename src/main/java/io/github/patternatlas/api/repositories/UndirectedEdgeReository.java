package io.github.patternatlas.api.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.patternatlas.api.entities.Pattern;
import io.github.patternatlas.api.entities.UndirectedEdge;

@RepositoryRestResource(exported = false)
public interface UndirectedEdgeReository extends CrudRepository<UndirectedEdge, UUID> {

    Optional<List<UndirectedEdge>> findByP1(Pattern pattern);

    Optional<List<UndirectedEdge>> findByP2(Pattern pattern);
}
