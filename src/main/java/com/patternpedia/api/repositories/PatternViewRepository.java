package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.PatternView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PatternViewRepository extends JpaRepository<PatternView, UUID> {

    Optional<PatternView> findByUri(String uri);

    boolean existsByUri(String uri);
}
