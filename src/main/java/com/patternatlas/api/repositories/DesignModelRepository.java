package com.patternatlas.api.repositories;

import com.patternatlas.api.entities.designmodel.DesignModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DesignModelRepository extends JpaRepository<DesignModel, UUID> {

    Optional<DesignModel> findByUri(String uri);

    boolean existsByUri(String uri);

    boolean existsById(UUID designModelId);
}
