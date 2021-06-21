package io.github.ust.quantil.patternatlas.api.service;

import java.util.List;
import java.util.UUID;

import io.github.ust.quantil.patternatlas.api.entities.user.UserEntity;

public interface UserService {

    UserEntity createUser(UserEntity user);

    UserEntity getUserById(UUID UserId);

    List<UserEntity> getAllUsers();

    UserEntity updateUser(UserEntity user);

    void deleteUser(UUID UserId);
}
