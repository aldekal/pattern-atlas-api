package io.github.patternatlas.api.service;

import io.github.patternatlas.api.entities.user.role.Privilege;
import io.github.patternatlas.api.entities.user.role.Role;
import io.github.patternatlas.api.entities.user.UserEntity;
import io.github.patternatlas.api.rest.model.user.RoleModel;
import io.github.patternatlas.api.rest.model.user.RoleModelRequest;
import io.github.patternatlas.api.rest.model.user.UserModel;
import io.github.patternatlas.api.rest.model.user.UserModelRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserEntity createUser(UserModelRequest userModelRequest);
    List<UserEntity> getAllUsers();
    UserEntity getUserById(UUID UserId);
    UserEntity updateUser(UUID userId, UserModelRequest userModelRequest);
    void deleteUser(UUID UserId);

    /** Role */
    List<Role> getAllRoles();
    List<Privilege> getAllPrivileges();
    Role updateRole(UUID roleId, UUID privilegeId, RoleModelRequest roleModelRequest);
}