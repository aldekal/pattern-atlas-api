package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.PatternGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PatternGraphRepository extends CrudRepository<PatternGraph, UUID> {
}
