package io.github.patternatlas.api.repositories;

import io.github.patternatlas.api.entities.user.role.Role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    boolean existsByName(String name);
    Role findByName(String name);
}
