package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.DirectedHyperedge;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface DirectedHyperedgeRepository extends CrudRepository<DirectedHyperedge, UUID> {
}
