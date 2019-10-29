package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.UndirectedEdge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.UUID;

@CrossOrigin
@RepositoryRestResource
public interface UndirectedEdgeReository extends CrudRepository<UndirectedEdge, UUID> {
}
