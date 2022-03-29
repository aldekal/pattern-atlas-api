package io.github.patternatlas.api.repositories;

import io.github.patternatlas.api.entities.user.role.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    boolean existsByName(String name);
    Role findByName(String name);

    @Query(value = "SELECT * FROM role r WHERE r.name IN (:names)", nativeQuery = true)
    public List<Role> findAllRolesByNames(@Param("names") List<String> names);

    @Query(value = "SELECT * FROM role r WHERE r.name like %:entityId", nativeQuery = true)
    public List<Role> findAllFromEntity(@Param("entityId") UUID entityId);

    @Query(value = "SELECT * from role r where r.name like %:entityId and r.name like :authorRole%", nativeQuery = true)
    public List<Role> findAllFromEntityForAuthorType(@Param("entityId") UUID entityId, @Param("authorRole") String authorRole);

    @Modifying
    @Query(value = "DELETE FROM role r WHERE r.name like %:entityId", nativeQuery = true)
    public void deleteAllFromEntity(@Param("entityId") UUID entityId);

}
