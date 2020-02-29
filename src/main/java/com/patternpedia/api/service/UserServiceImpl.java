package com.patternpedia.api.service;

import com.patternpedia.api.entities.user.UserEntity;
import com.patternpedia.api.exception.NullPatternException;
import com.patternpedia.api.exception.PatternLanguageNotFoundException;
import com.patternpedia.api.exception.PatternNotFoundException;
import com.patternpedia.api.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserEntity createUser(UserEntity user) {
        if (null == user) {
            throw new NullPatternException();
        }

//        if (null == patternEvolution.getPatternLanguage()) {
//            throw new NullPatternLanguageException();
//        }

        return this.userRepository.save(user);
    }

    @Override
    @Transactional
    public UserEntity updateUser(UserEntity user) {
        if (null == user) {
            throw new NullPatternException();
        }
        if (!this.userRepository.existsById(user.getId())) {
            throw new PatternLanguageNotFoundException(String.format("User %s not found", user.getId()));
        }

//        this.patternEvolutionRepository.

        return this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        UserEntity user =  this.getUserById(userId);
        if (null == user) {
            throw new NullPatternException();
        }

        // patternEvolution.setPatternViews(null);
        // this.patternEvolutionRepository.save(patternEvolution);
        this.userRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getUserById(UUID userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new PatternNotFoundException(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserEntity> getAllUsers() {
        return this.userRepository.findAll();
    }
}
