package io.github.patternatlas.api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.patternatlas.api.entities.designmodel.DesignModel;

public interface DesignModelRepository extends JpaRepository<DesignModel, UUID> {

    Optional<DesignModel> findByUri(String uri);

    boolean existsByUri(String uri);

    boolean existsById(UUID designModelId);
}
