package io.github.patternatlas.api.repositories;

import io.github.patternatlas.api.entities.user.role.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    public List<Role> findAllFromEntityForAuthorRole(@Param("entityId") UUID entityId, @Param("authorRole") String authorRole);

    @Query(value = "SELECT * from role r where r.name like :authorRole%", nativeQuery = true)
    public List<Role> findAllForAuthorRole(@Param("authorRole") String authorRole);

    @Modifying
    @Query(value = "DELETE FROM role r WHERE r.name like %:entityId", nativeQuery = true)
    public void deleteAllFromEntity(@Param("entityId") UUID entityId);

    @Query(value = "" +
            "SELECT CASE\n" +
            "    WHEN count(r.name) > 0\n" +
            "    THEN true\n" +
            "    ELSE false\n" +
            "END\n" +
            "FROM role r\n" +
            "JOIN role_privileges rp on r.id = rp.roles_id\n" +
            "JOIN privilege p on rp.privileges_id = p.id\n" +
            "WHERE r.id = :roleId\n" +
            "AND p.name = :privilegeName",
            nativeQuery = true)
    public boolean existsPrivilegeForRole(@Param("privilegeName") String privilegeName, @Param("roleId") UUID roleId);
}
