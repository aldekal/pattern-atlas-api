package io.github.ust.quantil.patternatlas.api.repositories;

import io.github.ust.quantil.patternatlas.api.entities.designmodel.ConcreteSolution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConcreteSolutionRepository extends JpaRepository<ConcreteSolution, UUID> {

    List<ConcreteSolution> findAllByPatternUri(String uri);

    Optional<ConcreteSolution> findTopByPatternUriAndAggregatorType(String uri, String technology);

    Optional<ConcreteSolution> findTopById(UUID uuid);

    boolean existsByPatternUri(URI uri);
}
