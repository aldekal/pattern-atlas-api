package io.github.patternatlas.api.repositories;

import io.github.patternatlas.api.entities.user.role.Privilege;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface PrivilegeRepository extends JpaRepository<Privilege, UUID> {

    public Optional<Privilege> findByName(String name);

    @Modifying
    @Query(value = "DELETE FROM privilege p WHERE p.name like %:resourceId", nativeQuery = true)
    public void deleteAllByResourceId(@Param("resourceId") UUID resourceId);
}
