package com.patternpedia.api.service;

import com.patternpedia.api.entities.user.role.Privilege;
import com.patternpedia.api.entities.user.role.Role;
import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.rest.model.user.RoleModel;
import com.patternpedia.api.rest.model.user.RoleModelRequest;
import com.patternpedia.api.rest.model.user.UserModel;
import com.patternpedia.api.rest.model.user.UserModelRequest;

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
