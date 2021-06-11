package com.patternatlas.api.service;

import com.patternatlas.api.entities.user.UserEntity;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserEntity createUser(UserEntity user);

    UserEntity getUserById(UUID UserId);

    List<UserEntity> getAllUsers();

    UserEntity updateUser(UserEntity user);

    void deleteUser(UUID UserId);
}
