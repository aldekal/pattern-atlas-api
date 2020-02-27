package com.patternpedia.api.repositories;

import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.UserEntity;
import com.patternpedia.api.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

}
