package com.patternpedia.demo.repositories;

import com.patternpedia.demo.entities.PatternSection;
import org.springframework.data.repository.CrudRepository;

public interface PatternSectionRepository extends CrudRepository<PatternSection, Long> {
}
