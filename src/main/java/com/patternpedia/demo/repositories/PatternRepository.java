package com.patternpedia.demo.repositories;

import com.patternpedia.demo.entities.Pattern;
import org.springframework.data.repository.CrudRepository;

public interface PatternRepository extends CrudRepository<Pattern, String> {
}
