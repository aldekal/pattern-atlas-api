package com.patternpedia.api.repositories;

import java.util.List;
import java.util.UUID;

import com.patternpedia.api.entities.PatternViewPattern;
import com.patternpedia.api.entities.PatternViewPatternId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PatternViewPatternRepository extends JpaRepository<PatternViewPattern, PatternViewPatternId> {
    List<PatternViewPattern> findAllByPatternViewId(UUID patternViewId);

    List<PatternViewPattern> findAllByPatternId(UUID patternId);
}
