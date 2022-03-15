package io.github.patternatlas.api.service;

import io.github.patternatlas.api.entities.candidate.Candidate;
import io.github.patternatlas.api.entities.user.role.RoleConstant;
import io.github.patternatlas.api.entities.user.role.Privilege;
import io.github.patternatlas.api.entities.user.role.Role;
import io.github.patternatlas.api.entities.user.UserEntity;
import io.github.patternatlas.api.exception.*;
import io.github.patternatlas.api.repositories.PrivilegeRepository;
import io.github.patternatlas.api.repositories.RoleRepository;
import io.github.patternatlas.api.repositories.UserRepository;
import io.github.patternatlas.api.rest.model.user.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PrivilegeRepository privilegeRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserEntity saveUser(UserEntity user) {
        return this.userRepository.save(user);
    }

    @Override
    @Transactional
    public UserEntity createUser(UserModelRequest userModelRequest) {
        if (null == userModelRequest)
            throw new RuntimeException("User to update is null!");
        // DEFAULT password
        if (userModelRequest.getPassword() == null)
            throw new RuntimeException("Password is null");


        UserEntity user = new UserEntity(userModelRequest,  passwordEncoder.encode(userModelRequest.getPassword()));
        if(userModelRequest.getRoles() != null) {
            userModelRequest.getRoles().stream().forEach(role -> {
                if (this.roleRepository.findByName(role) != null) {
                    user.getRoles().add(this.roleRepository.findByName(role));
                } else {
                    user.getRoles().add(this.roleRepository.findByName(RoleConstant.MEMBER));
                }
            });
        } else {
            // It could be that the UI only sends one singular role (not a set):
            if(userModelRequest.getRole() != null) {
                if (this.roleRepository.findByName(userModelRequest.getRole()) != null) {
                    user.getRoles().add(this.roleRepository.findByName(userModelRequest.getRole()));
                } else {
                    user.getRoles().add(this.roleRepository.findByName(RoleConstant.MEMBER));
                }
            }
        }

        
        return this.userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserEntity> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getUserById(UUID userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with ID %s not found!", userId)));
    }

    @Override
    @Transactional
    public UserEntity updateUser(UUID userId, UserModelRequest userModelRequest) {
        if (userModelRequest == null)
            throw new RuntimeException("User to update is null!");
        userModelRequest.getRoles().stream().forEach(role -> {
            if (!this.roleRepository.existsByName(role))
                throw new ResourceNotFoundException(String.format("User Role %s not found!", role));
        });

        UserEntity user = this.getUserById(userId);
        userModelRequest.getRoles().stream().forEach(role -> {
            if (!user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList()).contains(role))
                user.getRoles().add(this.roleRepository.findByName(role));
        });
        if (userModelRequest.getOldPassword() != null || userModelRequest.getPassword() != null) {
            if (!passwordEncoder.matches(userModelRequest.getOldPassword(), user.getPassword()) && !user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()).contains(RoleConstant.ADMIN))
                throw new ResourceNotFoundException(String.format("Password ERROR"));
            user.setPassword(passwordEncoder.encode(userModelRequest.getPassword()));
        }

        user.updateUserEntity(userModelRequest);
        return this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        this.getUserById(userId);
        this.userRepository.deleteById(userId);
    }

    /**
     * Role && Permission
     */
    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return this.roleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllPlatformRoles() {
        return this.roleRepository.findAllRolesByNames(Arrays.asList("ADMIN", "MEMBER", "EXPERT", "LIBRARIAN"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllAuthorRoles() {
        return this.roleRepository.findAllRolesByNames(Arrays.asList("HELPER", "MAINTAINER", "OWNER"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllRolesFromEntity(UUID entityId) {
        return this.roleRepository.findAllFromEntity(entityId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Privilege> getAllPlatformPrivileges() {
        return this.privilegeRepository.findAllPlatformPrivileges();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Privilege> getAllPrivilegesFromEntity(UUID entityId) {
        return this.privilegeRepository.findAllFromEntity(entityId);
    }

    @Override
    @Transactional
    public Role updateRole(UUID roleId, UUID privilegeId, RoleModelRequest roleModelRequest) {
        if (roleModelRequest == null)
            throw new RuntimeException("Role to update is null!");

        Role role = this.roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException(String.format(" Role %s not found!", roleId)));
        Privilege privilege = this.privilegeRepository.findById(privilegeId).orElseThrow(() -> new ResourceNotFoundException(String.format("Privilege %s not found!", privilegeId)));

        if (role.getPrivileges().contains(privilege) && !roleModelRequest.isCheckboxValue())
            role.getPrivileges().remove(privilege);

        if (!role.getPrivileges().contains(privilege) && roleModelRequest.isCheckboxValue())
            role.getPrivileges().add(privilege);

        return this.roleRepository.save(role);
    }
}