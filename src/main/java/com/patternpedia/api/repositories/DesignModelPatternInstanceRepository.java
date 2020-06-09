package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.designmodel.DesignModelPatternInstance;
import com.patternpedia.api.entities.designmodel.DesignModelPatternId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface DesignModelPatternInstanceRepository extends JpaRepository<DesignModelPatternInstance, DesignModelPatternId> {

    Optional<List<DesignModelPatternInstance>> findAllByDesignModelId(UUID patternViewId);

    List<DesignModelPatternInstance> findAllByPatternId(UUID patternId);
}
