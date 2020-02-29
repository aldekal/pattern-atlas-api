package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.pattern.evolution.CommentPatternEvolution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentPatternEvolutionRepository extends JpaRepository<CommentPatternEvolution, UUID> {

}
