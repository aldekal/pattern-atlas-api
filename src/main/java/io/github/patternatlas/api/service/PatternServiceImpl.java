package io.github.patternatlas.api.service;

import java.util.UUID;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import io.github.patternatlas.api.entities.Pattern;
import io.github.patternatlas.api.exception.NullPatternException;
import io.github.patternatlas.api.exception.NullPatternLanguageException;
import io.github.patternatlas.api.exception.PatternNotFoundException;
import io.github.patternatlas.api.repositories.PatternRepository;
import io.github.patternatlas.api.validator.PatternContentConstraint;

@Service
@Validated
@Transactional
public class PatternServiceImpl implements PatternService {

    private final PatternRepository patternRepository;

    Logger logger = LoggerFactory.getLogger(PatternServiceImpl.class);

    public PatternServiceImpl(PatternRepository patternRepository) {
        this.patternRepository = patternRepository;
    }

    @Override
    @Transactional
    public Pattern createPattern(@Valid @PatternContentConstraint Pattern pattern) {
        if (null == pattern) {
            throw new NullPatternException();
        }
        if (null == pattern.getPatternLanguage()) {
            throw new NullPatternLanguageException();
        }

        return this.patternRepository.save(pattern);
    }

    @Override
    @Transactional(noRollbackFor = Exception.class)
    public Pattern updatePattern(@Valid @PatternContentConstraint Pattern pattern) {
        if (null == pattern) {
            throw new NullPatternException();
        }
        if (null == pattern.getPatternLanguage()) {
            throw new NullPatternLanguageException();
        }

        return this.patternRepository.save(pattern);
    }

    @Override
    @Transactional(noRollbackFor = Exception.class)
    public void deletePattern(Pattern pattern) {
        if (null == pattern) {
            throw new NullPatternException();
        }

        pattern.setPatternViews(null);
        this.patternRepository.save(pattern);
        this.patternRepository.deleteById(pattern.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Pattern getPatternById(UUID patternId) {
        return this.patternRepository.findById(patternId)
                .orElseThrow(() -> new PatternNotFoundException(patternId));
    }

    @Override
    @Transactional(readOnly = true)
    public Pattern getPatternByUri(String uri) {
        return this.patternRepository.findByUri(uri)
                .orElseThrow(() -> new PatternNotFoundException(String.format("Pattern with URI %s not found!", uri)));
    }
}
