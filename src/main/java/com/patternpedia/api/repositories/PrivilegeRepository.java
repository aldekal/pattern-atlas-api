package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.user.role.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PrivilegeRepository extends JpaRepository<Privilege, UUID> {

    Privilege findByName(String name);
}
