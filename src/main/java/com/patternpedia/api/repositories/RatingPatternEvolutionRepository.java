package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.PatternEvolution;
import com.patternpedia.api.entities.PatternViewPattern;
import com.patternpedia.api.entities.PatternViewPatternId;
import com.patternpedia.api.entities.UserEntity;
import com.patternpedia.api.entities.rating.RatingKey;
import com.patternpedia.api.entities.rating.RatingUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface RatingUpRepository extends JpaRepository<RatingUp, RatingKey> {

    List<RatingUp> findAllByPatternEvolutionUp(PatternEvolution patternEvolution);

//    List<PatternEvolution> findAllByPatternEvolutionUp(PatternEvolution patternEvolutionUp);

    List<UserEntity> findAllByUserUp(UserEntity userUp);
}
