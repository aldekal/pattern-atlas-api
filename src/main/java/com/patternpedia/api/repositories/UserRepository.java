package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

}
