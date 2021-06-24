package io.github.patternatlas.api.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.patternatlas.api.entities.PatternViewPattern;
import io.github.patternatlas.api.entities.PatternViewPatternId;

@RepositoryRestResource(exported = false)
public interface PatternViewPatternRepository extends JpaRepository<PatternViewPattern, PatternViewPatternId> {
    List<PatternViewPattern> findAllByPatternViewId(UUID patternViewId);

    List<PatternViewPattern> findAllByPatternId(UUID patternId);
}
