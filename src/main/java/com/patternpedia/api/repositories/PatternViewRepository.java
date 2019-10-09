package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.PatternView;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface PatternViewRepository extends CrudRepository<PatternView, UUID> {
    public Optional<PatternView> findByUri(String uri);

    public boolean existsByUri(String uri);
}
