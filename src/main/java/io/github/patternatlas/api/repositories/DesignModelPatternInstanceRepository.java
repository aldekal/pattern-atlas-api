package io.github.patternatlas.api.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.patternatlas.api.entities.designmodel.DesignModelPatternInstance;

@RepositoryRestResource(exported = false)
public interface DesignModelPatternInstanceRepository extends JpaRepository<DesignModelPatternInstance, UUID> {

    Optional<List<DesignModelPatternInstance>> findAllByDesignModelId(UUID patternViewId);

    Optional<DesignModelPatternInstance> findTopByDesignModel_IdAndPatternInstanceId(UUID designModelId, UUID patternInstanceId);

    void deleteAllByDesignModel_IdAndPatternInstanceId(UUID designModelId, UUID patternInstanceId);
}
