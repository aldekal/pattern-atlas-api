package io.github.patternatlas.api.service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import io.github.patternatlas.api.entities.user.role.Privilege;
import io.github.patternatlas.api.entities.user.role.Role;
import io.github.patternatlas.api.repositories.RoleRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    public RoleServiceImpl(
            RoleRepository roleRepository
    ) {
        this.roleRepository = roleRepository;
    }

    /**
     * CRUD Role
     */
    @Override
    @Transactional
    public Role createRole(String name, Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

    @Override
    @Transactional
    public void deleteAllRolesByResourceId(UUID entityId) {
        this.roleRepository.deleteAllFromEntity(entityId);
    }

    @Override
    public List<Role> findAllFromEntityForAuthorRole(UUID entityId, String authorRole) {
        return this.roleRepository.findAllFromEntityForAuthorRole(entityId, authorRole);
    }

    @Override
    public boolean hasAnyPrivilege(UUID roleId, String... privileges) {
        for (String permission : privileges) {
            if (this.roleRepository.existsPrivilegeForRole(permission, roleId)) {
                return true;
            }
        }
        return false;
    }
}
