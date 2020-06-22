package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.designmodel.DesignModelPatternEdge;
import com.patternpedia.api.entities.designmodel.DesignModelPatternEdgeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface DesignModelPatternEdgeRepository extends JpaRepository<DesignModelPatternEdge, DesignModelPatternEdgeId> {

    Optional<List<DesignModelPatternEdge>> findAllByDesignModelId(UUID patternViewId);

    Optional<DesignModelPatternEdge> findTopByDesignModelIdAndEdgeId(UUID designModelId, DesignModelPatternEdgeId designModelPatternEdgeId);
}
