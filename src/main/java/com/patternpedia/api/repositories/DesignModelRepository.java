package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.designmodel.DesignModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DesignModelRepository extends JpaRepository<DesignModel, UUID> {

    Optional<DesignModel> findByUri(String uri);

    boolean existsByUri(String uri);
}
