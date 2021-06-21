package io.github.ust.quantil.patternatlas.api.repositories;

import io.github.ust.quantil.patternatlas.api.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

}
