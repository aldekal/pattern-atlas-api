package com.patternatlas.api.repositories;

import com.patternatlas.api.entities.Pattern;
import com.patternatlas.api.entities.UndirectedEdge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface UndirectedEdgeReository extends CrudRepository<UndirectedEdge, UUID> {

    Optional<List<UndirectedEdge>> findByP1(Pattern pattern);

    Optional<List<UndirectedEdge>> findByP2(Pattern pattern);

}
