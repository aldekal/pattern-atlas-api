package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.DirectedEdge;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface DirectedEdgeRepository extends CrudRepository<DirectedEdge, UUID> {
}
