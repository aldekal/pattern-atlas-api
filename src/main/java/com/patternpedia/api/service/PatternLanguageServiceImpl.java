package com.patternpedia.api.service;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.PatternSchema;
import com.patternpedia.api.exception.NullPatternLanguageException;
import com.patternpedia.api.exception.NullPatternSchemaException;
import com.patternpedia.api.exception.PatternLanguageNotFoundException;
import com.patternpedia.api.exception.PatternNotFoundException;
import com.patternpedia.api.repositories.PatternLanguageRepository;
import com.patternpedia.api.repositories.PatternRepository;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatternLanguageServiceImpl implements PatternLanguageService {

    private PatternSchemaService patternSchemaService;

    private PatternLanguageRepository patternLanguageRepository;
    private PatternRepository patternRepository;

    public PatternLanguageServiceImpl(PatternSchemaService patternSchemaService,
                                      PatternLanguageRepository patternLanguageRepository,
                                      PatternRepository patternRepository) {
        this.patternSchemaService = patternSchemaService;
        this.patternLanguageRepository = patternLanguageRepository;
        this.patternRepository = patternRepository;
    }

    @Override
    public PatternLanguage createPatternLanguage(PatternLanguage patternLanguage) {
        if (null == patternLanguage) {
            throw new NullPatternLanguageException("PatternLanguage is null");
        }

        PatternSchema patternSchema = patternLanguage.getPatternSchema();
        patternLanguage.setPatternSchema(null);
        patternLanguage = this.patternLanguageRepository.save(patternLanguage);
        if (null != patternSchema) {
            patternSchema.setPatternLanguage(patternLanguage);
            patternLanguage.setPatternSchema(patternSchema);
            return this.patternLanguageRepository.save(patternLanguage);
        } else {
            return this.patternLanguageRepository.save(patternLanguage);
        }
    }

    @Override
    public PatternLanguage updatePatternLanguage(PatternLanguage patternLanguage) {
        if (null == patternLanguage) {
            throw new NullPatternLanguageException();
        }

        if (!this.patternLanguageRepository.existsById(patternLanguage.getId())) {
            throw new PatternLanguageNotFoundException(String.format("PatternLanguage %s not found", patternLanguage.getId()));
        }

        // Persist new patterns
        if (null != patternLanguage.getPatterns()) {
            List<Pattern> patterns = patternLanguage.getPatterns().stream()
                    .map(pattern -> {
                        if (!this.patternRepository.existsById(pattern.getId())) {
                            pattern.setPatternLanguage(patternLanguage);
                            return this.patternRepository.save(pattern);
                        } else {
                            return pattern;
                        }
                    })
                    .collect(Collectors.toList());
            patternLanguage.setPatterns(patterns);
        }

        return this.patternLanguageRepository.save(patternLanguage);
    }

    @Override
    public void deletePatternLanguage(PatternLanguage patternLanguage) {
        throw new NotImplementedException();
    }

    @Override
    public List<Pattern> getAllPatternsOfPatternLanguage(UUID patternLanguageId) {
        PatternLanguage patternLanguage = this.getPatternLanguageById(patternLanguageId);

        return patternLanguage.getPatterns();
    }

    @Override
    public Pattern getPatternOfPatternLanguageById(UUID patternLanguageId, UUID patternId) {
        PatternLanguage patternLanguage = this.getPatternLanguageById(patternLanguageId);
        return patternLanguage.getPatterns()
                .stream()
                .filter(pattern -> pattern.getId().equals(patternId)).findFirst()
                .orElseThrow(() -> new PatternNotFoundException(
                        String.format("Pattern %s is not part of PatternLanguage %s", patternId.toString(), patternLanguageId.toString())
                ));
    }

    @Override
    public void deletePatternOfPatternLanguage(UUID patternLanguageId, UUID patternId) {
        if (!this.patternLanguageRepository.existsById(patternLanguageId)) {
            throw new PatternLanguageNotFoundException(String.format("PatternLanguage %s not found!", patternLanguageId));
        }
        Pattern pattern = this.patternRepository.findById(patternId)
                .orElseThrow(() -> new PatternNotFoundException(String.format("Pattern %s not found!", patternId)));

        // first clean up foreign key mappings
        pattern.setPatternLanguage(null);
        pattern.setPatternSchema(null);
        pattern.setPatternViews(new ArrayList<>());
        // Todo: Remove pattern from pattern views
        this.patternRepository.save(pattern);

        // then remove the pattern
        this.patternRepository.deleteById(pattern.getId());
    }

    @Override
    public List<PatternLanguage> getAllPatternLanguages() {
        return this.patternLanguageRepository.findAll();
    }

    @Override
    public PatternSchema createPatternSchemaAndAddToPatternLanguage(UUID patternLanguageId, PatternSchema patternSchema) {
        PatternLanguage patternLanguage = this.getPatternLanguageById(patternLanguageId);
        patternSchema.setPatternLanguage(patternLanguage);
        return this.patternSchemaService.createPatternSchema(patternSchema);
    }

    @Override
    public PatternLanguage getPatternLanguageById(UUID patternLanguageId) {
        return this.patternLanguageRepository.findById(patternLanguageId)
                .orElseThrow(() -> new PatternLanguageNotFoundException(String.format("PatternLanguage %s not found!", patternLanguageId)));
    }

    @Override
    public PatternLanguage getPatternLanguageByUri(String uri) {
        return this.patternLanguageRepository.findByUri(uri)
                .orElseThrow(() -> new PatternLanguageNotFoundException(String.format("PatternLanguage with URI %s not found!", uri)));
    }

    @Override
    public PatternSchema getPatternSchemaByPatternLanguageId(UUID patternLanguageId) {
        return this.getPatternLanguageById(patternLanguageId).getPatternSchema();
    }

    @Override
    public PatternSchema updatePatternSchemaByPatternLanguageId(UUID patternLanguageId, PatternSchema patternSchema) {
        if (null == patternSchema) {
            throw new NullPatternSchemaException();
        }
        PatternLanguage patternLanguage = this.getPatternLanguageById(patternLanguageId);
        patternSchema.setPatternLanguage(patternLanguage);
        return this.patternSchemaService.updatePatternSchema(patternSchema);
    }

}
