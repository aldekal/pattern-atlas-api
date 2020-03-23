package com.patternpedia.api.repositories;

import java.util.List;
import java.util.UUID;

import com.patternpedia.api.entities.pattern.view.PatternViewPattern;
import com.patternpedia.api.entities.pattern.view.PatternViewPatternId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface PatternViewPatternRepository extends JpaRepository<PatternViewPattern, PatternViewPatternId> {
    List<PatternViewPattern> findAllByPatternViewId(UUID patternViewId);

    List<PatternViewPattern> findAllByPatternId(UUID patternId);
}
