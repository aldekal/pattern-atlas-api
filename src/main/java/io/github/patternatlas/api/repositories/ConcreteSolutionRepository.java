package io.github.patternatlas.api.repositories;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.patternatlas.api.entities.designmodel.ConcreteSolution;

public interface ConcreteSolutionRepository extends JpaRepository<ConcreteSolution, UUID> {

    List<ConcreteSolution> findAllByPatternUri(String uri);

    Optional<ConcreteSolution> findTopByPatternUriAndAggregatorType(String uri, String technology);

    Optional<ConcreteSolution> findTopById(UUID uuid);

    boolean existsByPatternUri(URI uri);
}
