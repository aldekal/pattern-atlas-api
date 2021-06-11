package com.patternatlas.api.repositories;

import com.patternatlas.api.entities.designmodel.DesignModelEdgeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DesignModelEdgeTypeRepository extends JpaRepository<DesignModelEdgeType, String> {

    Optional<DesignModelEdgeType> findTopByName(String name);
}
