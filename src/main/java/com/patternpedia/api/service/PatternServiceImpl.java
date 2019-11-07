package com.patternpedia.api.service;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.exception.NullPatternException;
import com.patternpedia.api.exception.NullPatternLanguageException;
import com.patternpedia.api.exception.PatternNotFoundException;
import com.patternpedia.api.repositories.PatternRepository;
import com.patternpedia.api.validator.PatternContentConstraint;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.UUID;

@Validated
@Service
public class PatternServiceImpl implements PatternService {
    // Todo Add Validator for pattern creation, which checks if pattern is compliant to PatternSchema

    private PatternRepository patternRepository;

    public PatternServiceImpl(PatternRepository patternRepository) {
        this.patternRepository = patternRepository;
    }

    @Override
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
    public void deletePattern(Pattern pattern) {
        if (null == pattern) {
            throw new NullPatternException();
        }
        this.patternRepository.deleteById(pattern.getId());
    }

    @Override
    public Pattern getPatternById(UUID patternId) {
        return this.patternRepository.findById(patternId)
                .orElseThrow(() -> new PatternNotFoundException(String.format("Pattern %s not found!", patternId)));
    }

    @Override
    public Pattern getPatternByUri(String uri) {
        return this.patternRepository.findByUri(uri)
                .orElseThrow(() -> new PatternNotFoundException(String.format("Pattern with URI %s not found!", uri)));
    }
}
