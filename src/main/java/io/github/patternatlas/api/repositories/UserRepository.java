package io.github.patternatlas.api.repositories;

import java.util.UUID;

import org.apache.catalina.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.github.patternatlas.api.entities.user.UserEntity;

@RepositoryRestResource(exported = false)
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    UserEntity findByEmail(String email);
}
