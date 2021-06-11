package com.patternatlas.api.service;

import com.patternatlas.api.entities.user.UserEntity;
import com.patternatlas.api.exception.*;
import com.patternatlas.api.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserEntity createUser(UserEntity user) {
        if (null == user) {
            throw new NullUserException();
        }

        UserEntity newUser = this.userRepository.save(user);
        logger.info(String.format("Create Issue %s: ", newUser.toString()));
        return newUser;
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getUserById(UUID userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserEntity> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    @Transactional
    public UserEntity updateUser(UserEntity user) {
        if (null == user) {
            throw new NullUserException();
        }
        if (!this.userRepository.existsById(user.getId())) {
            throw new UserNotFoundException(user.getId());
        }

        return this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        UserEntity user =  this.getUserById(userId);
        if (null == user) {
            throw new NullUserException();
        }

        this.userRepository.deleteById(userId);
    }
}
