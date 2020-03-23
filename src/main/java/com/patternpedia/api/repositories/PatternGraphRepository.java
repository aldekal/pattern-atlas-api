package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.pattern.graph.PatternGraph;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface PatternGraphRepository extends CrudRepository<PatternGraph, String> {
}
