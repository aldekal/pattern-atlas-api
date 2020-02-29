package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.pattern.evolution.PatternEvolution;
import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.entities.rating.RatingKey;
import com.patternpedia.api.entities.rating.RatingPatternEvolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface RatingPatternEvolutionRepository extends JpaRepository<RatingPatternEvolution, RatingKey> {

    List<RatingPatternEvolution> findAllByPatternEvolution(PatternEvolution patternEvolution);

    List<UserEntity> findAllByUser(UserEntity user);

    RatingPatternEvolution findByPatternEvolutionAndUser(PatternEvolution patternEvolution, UserEntity user);

//    RatingPatternEvolution findById(RatingKey ratingKey);
}
