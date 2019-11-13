package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.UndirectedEdge;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UndirectedEdgeReository extends CrudRepository<UndirectedEdge, UUID> {
}
