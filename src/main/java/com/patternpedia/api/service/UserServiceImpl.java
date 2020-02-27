package com.patternpedia.api.service;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternEvolution;
import com.patternpedia.api.exception.NullPatternException;
import com.patternpedia.api.exception.PatternLanguageNotFoundException;
import com.patternpedia.api.exception.PatternNotFoundException;
import com.patternpedia.api.repositories.PatternEvolutionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PatternEvolutionServiceImpl implements PatternEvolutionService {

    private PatternEvolutionRepository patternEvolutionRepository;

    public PatternEvolutionServiceImpl(PatternEvolutionRepository patternEvolutionRepository) {
        this.patternEvolutionRepository = patternEvolutionRepository;
    }

    @Override
    @Transactional
    public PatternEvolution createPatternEvolution(PatternEvolution patternEvolution) {
        if (null == patternEvolution) {
            throw new NullPatternException();
        }

//        if (null == patternEvolution.getPatternLanguage()) {
//            throw new NullPatternLanguageException();
//        }

        return this.patternEvolutionRepository.save(patternEvolution);
    }

    @Override
    @Transactional
    public PatternEvolution updatePatternEvolution(PatternEvolution patternEvolution) {
        if (null == patternEvolution) {
            throw new NullPatternException();
        }
        if (!this.patternEvolutionRepository.existsById(patternEvolution.getId())) {
            throw new PatternLanguageNotFoundException(String.format("PatternEvolution %s not found", patternEvolution.getId()));
        }

//        this.patternEvolutionRepository.

        return this.patternEvolutionRepository.save(patternEvolution);
    }

    @Override
    @Transactional
    public void deletePatternEvolution(UUID patternEvolutionId) {
        PatternEvolution patternEvolution =  this.getPatternEvolutionById(patternEvolutionId);
        if (null == patternEvolution) {
            throw new NullPatternException();
        }

        // patternEvolution.setPatternViews(null);
        // this.patternEvolutionRepository.save(patternEvolution);
        this.patternEvolutionRepository.deleteById(patternEvolutionId);
    }

    @Override
    @Transactional(readOnly = true)
    public PatternEvolution getPatternEvolutionById(UUID patternEvolutionId) {
        return this.patternEvolutionRepository.findById(patternEvolutionId)
                .orElseThrow(() -> new PatternNotFoundException(patternEvolutionId));
    }

    @Override
    @Transactional(readOnly = true)
    public PatternEvolution getPatternEvolutionByUri(String uri) {
        return this.patternEvolutionRepository.findByUri(uri)
                .orElseThrow(() -> new PatternNotFoundException(String.format("Pattern with URI %s not found!", uri)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatternEvolution> getAllPatternEvolutions() {
        return this.patternEvolutionRepository.findAll();
    }
}
