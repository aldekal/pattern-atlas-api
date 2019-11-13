package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.PatternGraph;
import org.springframework.data.repository.CrudRepository;

public interface PatternGraphRepository extends CrudRepository<PatternGraph, String> {
}
