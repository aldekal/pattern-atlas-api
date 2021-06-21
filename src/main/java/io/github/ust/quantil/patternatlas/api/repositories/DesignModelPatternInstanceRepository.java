package io.github.ust.quantil.patternatlas.api.repositories;

import io.github.ust.quantil.patternatlas.api.entities.designmodel.DesignModelPatternInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface DesignModelPatternInstanceRepository extends JpaRepository<DesignModelPatternInstance, UUID> {

    Optional<List<DesignModelPatternInstance>> findAllByDesignModelId(UUID patternViewId);

    Optional<DesignModelPatternInstance> findTopByDesignModel_IdAndPatternInstanceId(UUID designModelId, UUID patternInstanceId);

    void deleteAllByDesignModel_IdAndPatternInstanceId(UUID designModelId, UUID patternInstanceId);
}
