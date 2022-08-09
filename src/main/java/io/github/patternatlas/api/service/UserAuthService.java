package io.github.patternatlas.api.service;

import io.github.patternatlas.api.entities.user.UserEntity;
import io.github.patternatlas.api.rest.model.user.UserModelRequest;

import java.util.UUID;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for manipulating users based on incomping JW-Tokens.
 * Not to be used for REST-Access.
 * Used to query user information to handle authentication.
 */
public interface UserAuthService {

    @Transactional
    UserEntity createInitialMember(UserModelRequest userModelRequest);

    @Transactional
    UserEntity createInitialAdmin(UserModelRequest userModelRequest);

    boolean userExists(UUID userId);

    /**
     * Checks if there are users in the db already
     * @return
     */
    boolean hasUsers();
}
