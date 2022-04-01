package io.github.patternatlas.api.security;


import io.github.patternatlas.api.entities.user.UserEntity;
import io.github.patternatlas.api.entities.user.role.Privilege;
import io.github.patternatlas.api.entities.user.role.Role;
import io.github.patternatlas.api.service.UserService;
import org.apache.catalina.User;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ResourceSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private UserService userService;

    public ResourceSecurityExpressionRoot(Authentication authentication,
                                          UserService userService) {
        super(authentication);
        this.userService = userService;
    }

    /**
     * Checks global permission for user.
     * Will only check for the exact permission (e.g. ISSUE_CREATE)
     * @param permissionType type of the permission (ISSUE_CREATE)
     * @return true if user has permission
     */
    public boolean hasGlobalPermission(String permissionType) {
        UUID userId = UUID.fromString(this.getAuthentication().getName()); // Supplied through JWT id field

        return this.userService.hasAnyPrivilege(
                userId,
                permissionType);
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
        UUID userId = UUID.fromString(this.getAuthentication().getName()); // Supplied through JWT id field

        return this.userService.hasAnyPrivilege(
                userId,
                permissionType + "_ALL",
                permissionType + "_" + resource.toString());
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
