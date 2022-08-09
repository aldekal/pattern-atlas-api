package io.github.patternatlas.api.security;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.patternatlas.api.entities.user.UserEntity;
import io.github.patternatlas.api.entities.user.role.Role;
import io.github.patternatlas.api.entities.user.role.RoleConstant;
import io.github.patternatlas.api.exception.UserNotFoundException;
import io.github.patternatlas.api.repositories.RoleRepository;
import io.github.patternatlas.api.rest.model.user.RoleModel;
import io.github.patternatlas.api.rest.model.user.UserModelRequest;
import io.github.patternatlas.api.service.RoleService;
import io.github.patternatlas.api.service.UserAuthService;
import io.github.patternatlas.api.service.UserService;
import io.github.patternatlas.api.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ResourceSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private UserAuthService userAuthService;
    private UserService userService;
    private RoleService roleService;
    private RoleRepository roleRepository;

    public ResourceSecurityExpressionRoot(Authentication authentication,
                                          UserService userService,
                                          RoleService roleService,
                                          RoleRepository roleRepository,
                                          UserAuthService userAuthService) {
        super(authentication);
        this.userService = userService;
        this.roleService = roleService;
        this.roleRepository = roleRepository;
        this.userAuthService = userAuthService;
    }

    /**
     * Checks if the user is logged in and makes sure the token-Information
     * is stored in patternatlas db.
     * @return true if the user is present in db and logged in, false otherwise
     */
    public boolean isLoggedIn() {
        Optional<UUID> userId = loggedInUUID();
        return userId.isPresent();
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

    private void createUser(UUID userId, Authentication authentication) {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) this.getAuthentication().getDetails();
        Jwt jwt = JwtHelper.decode(details.getTokenValue());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> claims = objectMapper.readValue(jwt.getClaims(), Map.class);
            String preferredUsername = claims.get("preferred_username").toString();
            UserModelRequest req = new UserModelRequest();
            req.setId(userId);
            req.setName(preferredUsername);
            try {
                if(this.userAuthService.hasUsers()) {
                    // Create standard member user
                    this.userAuthService.createInitialMember(req);
                } else {
                    // There are no other users registered => create admin user as first user
                    this.userAuthService.createInitialAdmin(req);
                }
            } catch (DataIntegrityViolationException e) {
                /*
                Is thrown if two simultaneous calls are the first calls a user makes to the API.
                In this case the second create user call will fail since both calls are made with
                the same ID. Since the call only fails if the user was properly created
                by the first call, this exception can be ignored.
                 */
            }
        } catch (JsonProcessingException e) {
            throw new UserNotFoundException("Cannot infer preferred username from Token");
        }
    }

    public Optional<UUID> loggedInUUID() {
        if (this.getAuthentication() == null) {
            return Optional.empty();
        }

        try {
            // Check if user exists in patternatlas
            UUID userId = UUID.fromString(this.getAuthentication().getName()); // Supplied through JWT id field
            if(!this.userAuthService.userExists(userId)) {
                // The user is properly authenticated but is not yet created in patternatlas db
                createUser(userId, this.getAuthentication());
            }
            return Optional.of(userId);
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

    public void setUserAuthService(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
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
