package com.patternpedia.demo.repositories;

import com.patternpedia.demo.entities.PatternGraph;
import org.springframework.data.repository.CrudRepository;

public interface PatternGraphRepository extends CrudRepository<PatternGraph, String> {
}
