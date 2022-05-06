package io.github.patternatlas.api.security;


import io.github.patternatlas.api.entities.user.role.RoleConstant;
import io.github.patternatlas.api.repositories.RoleRepository;
import io.github.patternatlas.api.service.RoleService;
import io.github.patternatlas.api.service.UserService;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.UUID;

public class ResourceSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private UserService userService;
    private RoleService roleService;
    private RoleRepository roleRepository;

    public ResourceSecurityExpressionRoot(Authentication authentication,
                                          UserService userService,
                                          RoleService roleService,
                                          RoleRepository roleRepository) {
        super(authentication);
        this.userService = userService;
        this.roleService = roleService;
        this.roleRepository = roleRepository;
    }

    /**
     * Checks global permission for user.
     * Will only check for the exact permission (e.g. ISSUE_CREATE)
     * @param permissionType type of the permission (ISSUE_CREATE)
     * @return true if user has permission
     */
    public boolean hasGlobalPermission(String permissionType) {
        Optional<UUID> userId = loggedInUUID();

        if (userId.isPresent()) {
            return this.userService.hasAnyPrivilege(
                    userId.get(),
                    permissionType);
        } else {
            return this.roleService.hasAnyPrivilege(
                roleRepository.findByName(RoleConstant.GUEST).getId(),
                permissionType
            );
        }
    }

    public Optional<UUID> loggedInUUID() {
        if (this.getAuthentication() == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(UUID.fromString(this.getAuthentication().getName())); // Supplied through JWT id field
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    /**
     * Checks permission given the objects UUID.
     * Will check for general version of the permission (e.g. ISSUE_READ_ALL)
     * and for resource specific version (e.g. ISSUE_READ_[uuid])
     * @param resource resource uuid
     * @param permissionType type of the permission (ISSUE_READ)
     * @return true if user has permission
     */
    public boolean hasResourcePermission(UUID resource, String permissionType) {
        Optional<UUID> userId = loggedInUUID();

        if (userId.isPresent()) {
            return this.userService.hasAnyPrivilege(
                    userId.get(),
                    permissionType + "_ALL",
                    permissionType + "_" + resource.toString());
        } else {
            return this.roleService.hasAnyPrivilege(
                    roleRepository.findByName(RoleConstant.GUEST).getId(),
                    permissionType + "_ALL",
                    permissionType + "_" + resource.toString()
            );
        }
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void setFilterObject(Object o) {
        this.filterObject = o;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object o) {
        this.returnObject = o;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }
}
