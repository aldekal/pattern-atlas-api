package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.UndirectedHyperedge;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UndirectedHyperedgeRepository extends CrudRepository<UndirectedHyperedge, UUID> {
}
