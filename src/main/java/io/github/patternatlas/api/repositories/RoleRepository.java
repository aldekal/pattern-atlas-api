package io.github.patternatlas.api.repositories;

import io.github.patternatlas.api.entities.user.role.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    boolean existsByName(String name);
    Role findByName(String name);

    @Modifying
    @Query(value = "DELETE FROM role r WHERE r.name like %:resourceId", nativeQuery = true)
    public void deleteAllByResourceId(@Param("resourceId") UUID resourceId);

}
