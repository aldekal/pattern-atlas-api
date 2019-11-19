package com.patternpedia.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patternpedia.api.entities.*;
import com.patternpedia.api.exception.NullPatternLanguageException;
import com.patternpedia.api.exception.NullPatternSchemaException;
import com.patternpedia.api.exception.PatternLanguageNotFoundException;
import com.patternpedia.api.exception.PatternNotFoundException;
import com.patternpedia.api.repositories.PatternLanguageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PatternLanguageServiceImpl implements PatternLanguageService {

    private PatternSchemaService patternSchemaService;
    private PatternService patternService;
    private PatternRelationDescriptorService patternRelationDescriptorService;
    private PatternViewService patternViewService;
    private PatternLanguageRepository patternLanguageRepository;
    private ObjectMapper objectMapper;

    public PatternLanguageServiceImpl(PatternSchemaService patternSchemaService,
                                      PatternService patternService,
                                      PatternRelationDescriptorService patternRelationDescriptorService,
                                      PatternViewService patternViewService,
                                      PatternLanguageRepository patternLanguageRepository,
                                      ObjectMapper objectMapper) {
        this.patternSchemaService = patternSchemaService;
        this.patternService = patternService;
        this.patternRelationDescriptorService = patternRelationDescriptorService;
        this.patternViewService = patternViewService;
        this.patternLanguageRepository = patternLanguageRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public PatternLanguage createPatternLanguage(PatternLanguage patternLanguage) {
        if (null == patternLanguage) {
            throw new NullPatternLanguageException("PatternLanguage is null");
        }

        if (null == patternLanguage.getPatternSchema()) {
            throw new NullPatternSchemaException("No PatternSchema defined!");
        }

        PatternSchema patternSchema = patternLanguage.getPatternSchema();
        patternLanguage.setPatternSchema(null);
        patternLanguage = this.patternLanguageRepository.save(patternLanguage);

        patternSchema.setPatternLanguage(patternLanguage);
        patternSchema = this.patternSchemaService.createPatternSchema(patternSchema);
        patternLanguage.setPatternSchema(patternSchema);

        return this.patternLanguageRepository.save(patternLanguage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatternLanguage> getPatternLanguages() {
        return this.patternLanguageRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PatternLanguage getPatternLanguageById(UUID patternLanguageId) {
        return this.patternLanguageRepository.findById(patternLanguageId)
                .orElseThrow(() -> new PatternLanguageNotFoundException(String.format("PatternLanguage %s not found!", patternLanguageId)));
    }

    @Override
    @Transactional(readOnly = true)
    public PatternLanguage getPatternLanguageByUri(String uri) {
        return this.patternLanguageRepository.findByUri(uri)
                .orElseThrow(() -> new PatternLanguageNotFoundException(String.format("PatternLanguage with URI %s not found!", uri)));
    }

    @Override
    @Transactional
    public PatternLanguage updatePatternLanguage(PatternLanguage patternLanguage) {
        // We just support updating fields of PatternLanguage but we don't support updates of sub resources such as Patterns or PatternSchema.
        // So we ignore patterns, schema and edges contained in given patternLanguage.
        if (null == patternLanguage) {
            throw new NullPatternLanguageException();
        }

        if (!this.patternLanguageRepository.existsById(patternLanguage.getId())) {
            throw new PatternLanguageNotFoundException(String.format("PatternLanguage %s not found", patternLanguage.getId()));
        }

        // Here we reset patternSchema, patterns, undirectedEdges and directedEdges to what is already stored
        patternLanguage.setPatternSchema(this.getPatternSchemaByPatternLanguageId(patternLanguage.getId()));
        patternLanguage.setPatterns(this.getPatternsOfPatternLanguage(patternLanguage.getId()));
        patternLanguage.setUndirectedEdges(this.getUndirectedEdgesOfPatternLanguage(patternLanguage.getId()));
        patternLanguage.setDirectedEdges(this.getDirectedEdgesOfPatternLanguage(patternLanguage.getId()));

        return this.patternLanguageRepository.save(patternLanguage);
    }

    @Override
    @Transactional
    public void deletePatternLanguage(UUID patternLanguageId) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional
    public Pattern createPatternAndAddToPatternLanguage(UUID patternLanguageId, Pattern pattern) {
        PatternLanguage patternLanguage = this.getPatternLanguageById(patternLanguageId);

        pattern.setId(null);
        pattern.setPatternLanguage(patternLanguage);

        pattern = this.patternService.createPattern(pattern);
        if (null != patternLanguage.getPatterns()) {
            patternLanguage.getPatterns().add(pattern);
        } else {
            patternLanguage.setPatterns(new ArrayList<>(Collections.singletonList(pattern)));
        }
        this.patternLanguageRepository.save(patternLanguage);

        return pattern;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pattern> getPatternsOfPatternLanguage(UUID patternLanguageId) {
        PatternLanguage patternLanguage = this.getPatternLanguageById(patternLanguageId);
        if (null == patternLanguage.getPatterns()) {
            patternLanguage.setPatterns(Collections.emptyList());
        }
        return patternLanguage.getPatterns();
    }

    @Override
    @Transactional(readOnly = true)
    public Pattern getPatternOfPatternLanguageById(UUID patternLanguageId, UUID patternId) {
        PatternLanguage patternLanguage = this.getPatternLanguageById(patternLanguageId);
        if (null == patternLanguage.getPatterns()) {
            throw new PatternNotFoundException(patternLanguage, patternId);
        }

        return patternLanguage.getPatterns()
                .stream()
                .filter(pattern -> pattern.getId().equals(patternId)).findFirst()
                .map(pattern -> {
                    if (null == pattern.getContent()) {
                        pattern.setContent(this.objectMapper.createObjectNode());
                    }
                    return pattern;
                })
                .orElseThrow(() -> new PatternNotFoundException(patternLanguage, patternId));
    }

    @Override
    @Transactional
    public void deletePatternOfPatternLanguage(UUID patternLanguageId, UUID patternId) {
        if (!this.patternLanguageRepository.existsById(patternLanguageId)) {
            throw new PatternLanguageNotFoundException(patternLanguageId);
        }

        Pattern pattern = this.patternService.getPatternById(patternId);
        PatternLanguage patternLanguage = this.getPatternLanguageById(patternLanguageId);

        if (null != pattern.getPatternLanguage()) {
            if (pattern.getPatternLanguage().getId() != patternLanguageId) {
                throw new PatternNotFoundException(patternLanguage, patternId);
            }
        }

        // 1.1 Remove DirectedEdges the pattern is included
        List<DirectedEdge> directedEdges = this.patternRelationDescriptorService.findDirectedEdgeBySource(pattern);
        directedEdges.addAll(this.patternRelationDescriptorService.findDirectedEdgeByTarget(pattern));
        this.patternRelationDescriptorService.deleteAllDirectedEdges(directedEdges);

        // 1.2 Remove UndirectedEdges the pattern is included
        List<UndirectedEdge> undirectedEdges = this.patternRelationDescriptorService.findUndirectedEdgeByPattern(pattern);
        this.patternRelationDescriptorService.deleteAllUndirectedEdges(undirectedEdges);

        // 2. Remove Pattern from Views it is included
        pattern.setPatternViews(null);
        pattern = this.patternService.updatePattern(pattern);

        // 3. Remove pattern
        this.patternService.deletePattern(pattern);
    }

    @Override
    @Transactional
    public PatternSchema createPatternSchemaAndAddToPatternLanguage(UUID patternLanguageId, PatternSchema patternSchema) {
        PatternLanguage patternLanguage = this.getPatternLanguageById(patternLanguageId);
        patternSchema.setPatternLanguage(patternLanguage);
        return this.patternSchemaService.createPatternSchema(patternSchema);
    }

    @Override
    @Transactional(readOnly = true)
    public PatternSchema getPatternSchemaByPatternLanguageId(UUID patternLanguageId) {
        return this.getPatternLanguageById(patternLanguageId).getPatternSchema();
    }

    @Override
    @Transactional
    public PatternSchema updatePatternSchemaOfPatternLanguage(UUID patternLanguageId, PatternSchema patternSchema) {
        if (null == patternSchema) {
            throw new NullPatternSchemaException();
        }
        PatternLanguage patternLanguage = this.getPatternLanguageById(patternLanguageId);
        patternSchema.setPatternLanguage(patternLanguage);
        return this.patternSchemaService.updatePatternSchema(patternSchema);
    }

    @Override
    @Transactional
    public DirectedEdge createDirectedEdgeAndAddToPatternLanguage(UUID patternLanguageId, DirectedEdge directedEdge) {
        PatternLanguage patternLanguage = this.patternLanguageRepository.findById(patternLanguageId)
                .orElseThrow(() -> new PatternLanguageNotFoundException(patternLanguageId));
        directedEdge.setPatternLanguage(patternLanguage);
        directedEdge = this.patternRelationDescriptorService.createDirectedEdge(directedEdge);
        if (null != patternLanguage.getDirectedEdges()) {
            patternLanguage.getDirectedEdges().add(directedEdge);
        }
        this.patternLanguageRepository.save(patternLanguage);
        return directedEdge;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DirectedEdge> getDirectedEdgesOfPatternLanguage(UUID patternLanguageId) {
        PatternLanguage patternLanguage = this.getPatternLanguageById(patternLanguageId);
        return patternLanguage.getDirectedEdges();
    }

    @Override
    @Transactional
    public UndirectedEdge createUndirectedEdgeAndAddToPatternLanguage(UUID patternLanguageId, UndirectedEdge undirectedEdge) {
        PatternLanguage patternLanguage = this.getPatternLanguageById(patternLanguageId);
        undirectedEdge.setPatternLanguage(patternLanguage);
        undirectedEdge = this.patternRelationDescriptorService.createUndirectedEdge(undirectedEdge);
        if (null != patternLanguage.getUndirectedEdges()) {
            patternLanguage.getUndirectedEdges().add(undirectedEdge);
        }
        this.patternLanguageRepository.save(patternLanguage);
        return undirectedEdge;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UndirectedEdge> getUndirectedEdgesOfPatternLanguage(UUID patternLanguageId) {
        PatternLanguage patternLanguage = this.getPatternLanguageById(patternLanguageId);
        return patternLanguage.getUndirectedEdges();
    }

}
