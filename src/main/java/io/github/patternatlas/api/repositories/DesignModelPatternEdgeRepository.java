package io.github.patternatlas.api.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.patternatlas.api.entities.designmodel.DesignModelPatternEdge;
import io.github.patternatlas.api.entities.designmodel.DesignModelPatternEdgeId;

@RepositoryRestResource(exported = false)
public interface DesignModelPatternEdgeRepository extends JpaRepository<DesignModelPatternEdge, DesignModelPatternEdgeId> {

    Optional<List<DesignModelPatternEdge>> findAllByDesignModelId(UUID patternViewId);

    Optional<DesignModelPatternEdge> findTopByDesignModelIdAndEdgeId(UUID designModelId, DesignModelPatternEdgeId designModelPatternEdgeId);

    void deleteAllByDesignModel_IdAndPatternInstance1_PatternInstanceIdOrPatternInstance2_PatternInstanceId(UUID designModelId, UUID patternInstanceId1, UUID patternInstanceId2);

    void deleteAllByDesignModel_IdAndPatternInstance1_PatternInstanceIdAndPatternInstance2_PatternInstanceId(UUID designModelId, UUID patternInstanceId1, UUID patternInstanceId2);
}
