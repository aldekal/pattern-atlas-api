package io.github.patternatlas.api.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.patternatlas.api.entities.PatternSchema;
import io.github.patternatlas.api.entities.PatternSectionSchema;
import io.github.patternatlas.api.exception.NullPatternSchemaException;
import io.github.patternatlas.api.exception.PatternSchemaNotFoundException;
import io.github.patternatlas.api.repositories.PatternSchemaRepository;
import io.github.patternatlas.api.repositories.PatternSectionSchemaRepository;

@Service
@Transactional
public class PatternSchemaServiceImpl implements PatternSchemaService {

    private final PatternSchemaRepository patternSchemaRepository;

    private final PatternSectionSchemaRepository patternSectionSchemaRepository;

    public PatternSchemaServiceImpl(PatternSchemaRepository patternSchemaRepository,
                                    PatternSectionSchemaRepository patternSectionSchemaRepository) {
        this.patternSchemaRepository = patternSchemaRepository;
        this.patternSectionSchemaRepository = patternSectionSchemaRepository;
    }

    @Override
    @Transactional
    public PatternSchema createPatternSchema(PatternSchema patternSchema) {
        if (null == patternSchema) {
            throw new NullPatternSchemaException();
        }
        List<PatternSectionSchema> patternSectionSchemas = patternSchema.getPatternSectionSchemas();
        patternSchema.setPatternSectionSchemas(null);
        patternSchema = this.patternSchemaRepository.save(patternSchema);

        if (null != patternSectionSchemas) {
            PatternSchema finalPatternSchema = patternSchema;
            List<PatternSectionSchema> persistedSectionSchemas = patternSectionSchemas.stream()
                    .map(patternSectionSchema -> {
                        patternSectionSchema.setPatternSchema(finalPatternSchema);
                        return this.patternSectionSchemaRepository.save(patternSectionSchema);
                    }).collect(toList());
            patternSchema.setPatternSectionSchemas(persistedSectionSchemas);
        }
        return this.patternSchemaRepository.save(patternSchema);
    }

    @Override
    @Transactional
    public PatternSchema updatePatternSchema(PatternSchema patternSchema) {
        if (null == patternSchema) {
            throw new NullPatternSchemaException("PatternSchema is null");
        }

        if (this.patternSchemaRepository.existsById(patternSchema.getId())) {
            return this.patternSchemaRepository.save(patternSchema);
        } else {
            throw new PatternSchemaNotFoundException(String.format("PatternSchema not found: %s", patternSchema.getId()));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PatternSchema getPatternSchemaById(UUID id) {
        return this.patternSchemaRepository.findById(id)
                .orElseThrow(() -> new PatternSchemaNotFoundException(String.format("PatternSchema not found: %s", id)));
    }

    @Override
    @Transactional
    public void deletePatternSchema(PatternSchema schema) {
        this.patternSchemaRepository.deleteById(schema.getId());
    }
}
