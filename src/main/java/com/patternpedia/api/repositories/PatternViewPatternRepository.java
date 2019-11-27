package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.PatternViewPattern;
import com.patternpedia.api.entities.PatternViewPatternId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PatternViewPatternRepository extends JpaRepository<PatternViewPattern, PatternViewPatternId> {
}
