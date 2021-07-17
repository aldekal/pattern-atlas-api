package io.github.patternatlas.api.repositories;

import io.github.patternatlas.api.entities.user.role.Privilege;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PrivilegeRepository extends JpaRepository<Privilege, UUID> {

    Privilege findByName(String name);
}
