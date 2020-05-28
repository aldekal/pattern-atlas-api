package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

}
