package com.patternpedia.api.service;

import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternLanguage;
import com.patternpedia.api.entities.UndirectedEdge;
import com.patternpedia.api.exception.DirectedEdgeNotFoundException;
import com.patternpedia.api.exception.NullPatternLanguageException;
import com.patternpedia.api.exception.UndirectedEdgeNotFoundException;
import com.patternpedia.api.repositories.DirectedEdgeRepository;
import com.patternpedia.api.repositories.UndirectedEdgeReository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PatternRelationDescriptorServiceImpl implements PatternRelationDescriptorService {

    private DirectedEdgeRepository directedEdgeRepository;

    private UndirectedEdgeReository undirectedEdgeReository;

    private PatternLanguageService patternLanguageService;

    public PatternRelationDescriptorServiceImpl(DirectedEdgeRepository directedEdgeRepository,
                                                UndirectedEdgeReository undirectedEdgeReository,
                                                PatternLanguageService patternLanguageService) {
        this.directedEdgeRepository = directedEdgeRepository;
        this.undirectedEdgeReository = undirectedEdgeReository;
        this.patternLanguageService = patternLanguageService;
    }

    @Override
    @Transactional
    public DirectedEdge createDirectedEdge(DirectedEdge directedEdge, UUID patternLanguageId) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);
        directedEdge.setPatternLanguage(patternLanguage);

        directedEdge = this.directedEdgeRepository.save(directedEdge);

        if (null != patternLanguage.getDirectedEdges()) {
            patternLanguage.getDirectedEdges().add(directedEdge);
        } else {
            patternLanguage.setDirectedEdges(new ArrayList<>(Collections.singletonList(directedEdge)));
        }
        this.patternLanguageService.updatePatternLanguage(patternLanguage);
        return directedEdge;
    }

    @Override
    @Transactional(readOnly = true)
    public DirectedEdge getDirectedEdgeById(UUID id) {
        return this.directedEdgeRepository.findById(id)
                .orElseThrow(() -> new DirectedEdgeNotFoundException(String.format("DirectedEdge %s not found!", id.toString())));
    }

    @Override
    public List<DirectedEdge> findDirectedEdgeBySource(Pattern pattern) {
        return this.directedEdgeRepository.findBySource(pattern)
                .orElseThrow(() -> new DirectedEdgeNotFoundException(String.format("No DirectedEdge found with Pattern %s as source", pattern.getId())));
    }

    @Override
    public List<DirectedEdge> findDirectedEdgeByTarget(Pattern pattern) {
        return this.directedEdgeRepository.findByTarget(pattern)
                .orElseThrow(() -> new DirectedEdgeNotFoundException(String.format("No DirectedEdge found with Pattern %s as target", pattern.getId())));
    }

    @Override
    @Transactional
    public void deleteDirectedEdge(UUID directedEdgeId) {
        DirectedEdge directedEdge = this.getDirectedEdgeById(directedEdgeId);

        if (null == directedEdge.getPatternLanguage()) {
            throw new NullPatternLanguageException();
        }

        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(directedEdge.getPatternLanguage().getId());
        if (null != patternLanguage.getDirectedEdges()) {
            patternLanguage.getDirectedEdges().remove(directedEdge);
        }
        this.patternLanguageService.updatePatternLanguage(patternLanguage);

        this.directedEdgeRepository.delete(directedEdge);
    }

    @Override
    @Transactional
    public UndirectedEdge createUndirectedEdge(UndirectedEdge undirectedEdge, UUID patternLanguageId) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);
        undirectedEdge.setPatternLanguage(patternLanguage);

        undirectedEdge = this.undirectedEdgeReository.save(undirectedEdge);

        if (null != patternLanguage.getDirectedEdges()) {
            patternLanguage.getUndirectedEdges().add(undirectedEdge);
        } else {
            patternLanguage.setUndirectedEdges(new ArrayList<>(Collections.singletonList(undirectedEdge)));
        }
        this.patternLanguageService.updatePatternLanguage(patternLanguage);
        return undirectedEdge;
    }

    @Override
    @Transactional(readOnly = true)
    public UndirectedEdge getUndirectedEdgeById(UUID id) {
        return this.undirectedEdgeReository.findById(id)
                .orElseThrow(() -> new UndirectedEdgeNotFoundException(String.format("UndirectedEdge %s not found!", id.toString())));
    }

    @Override
    @Transactional
    public void deleteUndirectedEdge(UUID undirectedEdgeId) {
        UndirectedEdge undirectedEdge = this.getUndirectedEdgeById(undirectedEdgeId);

        if (null == undirectedEdge.getPatternLanguage()) {
            throw new NullPatternLanguageException();
        }

        undirectedEdge = this.getUndirectedEdgeById(undirectedEdge.getId());

        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(undirectedEdge.getPatternLanguage().getId());
        if (null != patternLanguage.getDirectedEdges()) {
            patternLanguage.getUndirectedEdges().remove(undirectedEdge);
        }
        this.patternLanguageService.updatePatternLanguage(patternLanguage);

        this.undirectedEdgeReository.delete(undirectedEdge);
    }
}
