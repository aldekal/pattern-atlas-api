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

    List<Role> findAllFromEntityForAuthorType(UUID entityId, String authorRole);
}
