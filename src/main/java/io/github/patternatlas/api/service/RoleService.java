package io.github.patternatlas.api.service;

import io.github.patternatlas.api.entities.user.role.Role;
import io.github.patternatlas.api.entities.user.role.Privilege;

import java.util.List;
import java.util.UUID;
import java.util.Collection;

public interface RoleService {
    /** CRUD  */
    Role createRole(String name, Collection<Privilege> privileges);

    void deleteAllRolesByResourceId(UUID entityId);

    List<Role> findAllFromEntityForAuthorRole(UUID entityId, String authorRole);

    /**
     * Checks if a user has one of the supplied privileges, without querying all
     * available roles and privileges
     * @param roleId
     * @param privileges
     * @return true if one of the privileges is present for the user
     */
    boolean hasAnyPrivilege(UUID roleId, String ... privileges);
}
