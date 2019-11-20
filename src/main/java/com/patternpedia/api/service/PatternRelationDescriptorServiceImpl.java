package com.patternpedia.api.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.UndirectedEdge;
import com.patternpedia.api.exception.DirectedEdgeNotFoundException;
import com.patternpedia.api.exception.NullPatternLanguageException;
import com.patternpedia.api.exception.UndirectedEdgeNotFoundException;
import com.patternpedia.api.repositories.DirectedEdgeRepository;
import com.patternpedia.api.repositories.UndirectedEdgeReository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PatternRelationDescriptorServiceImpl implements PatternRelationDescriptorService {

    private DirectedEdgeRepository directedEdgeRepository;

    private UndirectedEdgeReository undirectedEdgeReository;

    public PatternRelationDescriptorServiceImpl(DirectedEdgeRepository directedEdgeRepository,
                                                UndirectedEdgeReository undirectedEdgeReository) {
        this.directedEdgeRepository = directedEdgeRepository;
        this.undirectedEdgeReository = undirectedEdgeReository;
    }

    @Override
    @Transactional
    public DirectedEdge createDirectedEdge(DirectedEdge directedEdge) {
        if (null == directedEdge.getPatternLanguage()) {
            throw new NullPatternLanguageException();
        }

        return this.directedEdgeRepository.save(directedEdge);
    }

    @Override
    @Transactional(readOnly = true)
    public DirectedEdge getDirectedEdgeById(UUID id) {
        return this.directedEdgeRepository.findById(id)
                .orElseThrow(() -> new DirectedEdgeNotFoundException(String.format("DirectedEdge %s not found!", id.toString())));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DirectedEdge> findDirectedEdgeBySource(Pattern pattern) {
        return this.directedEdgeRepository.findBySource(pattern)
                .orElseThrow(() -> new DirectedEdgeNotFoundException(String.format("No DirectedEdge found with Pattern %s as source", pattern.getId())));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DirectedEdge> findDirectedEdgeByTarget(Pattern pattern) {
        return this.directedEdgeRepository.findByTarget(pattern)
                .orElseThrow(() -> new DirectedEdgeNotFoundException(String.format("No DirectedEdge found with Pattern %s as target", pattern.getId())));
    }

    @Override
    @Transactional
    public DirectedEdge updateDirectedEdge(DirectedEdge directedEdge) {
        return this.directedEdgeRepository.save(directedEdge);
    }

    @Override
    @Transactional
    public void deleteDirectedEdgeById(UUID directedEdgeId) {
        DirectedEdge directedEdge = this.getDirectedEdgeById(directedEdgeId);
        this.deleteDirectedEdge(directedEdge);
    }

    @Override
    @Transactional
    public void deleteDirectedEdge(DirectedEdge directedEdge) {
        directedEdge.setPatternViews(null);
        this.updateDirectedEdge(directedEdge);
        this.directedEdgeRepository.delete(directedEdge);
    }

    @Override
    @Transactional
    public void deleteAllDirectedEdges(Iterable<DirectedEdge> directedEdges) {
        for (DirectedEdge directedEdge : directedEdges) {
            this.deleteDirectedEdge(directedEdge);
        }
    }

    @Override
    @Transactional
    public UndirectedEdge createUndirectedEdge(UndirectedEdge undirectedEdge) {
        return this.undirectedEdgeReository.save(undirectedEdge);
    }

    @Override
    @Transactional(readOnly = true)
    public UndirectedEdge getUndirectedEdgeById(UUID id) {
        return this.undirectedEdgeReository.findById(id)
                .orElseThrow(() -> new UndirectedEdgeNotFoundException(String.format("UndirectedEdge %s not found!", id.toString())));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UndirectedEdge> findUndirectedEdgeByPattern(Pattern pattern) {
        List<UndirectedEdge> undirectedEdges = new ArrayList<>();
        undirectedEdges.addAll(this.findUndirectedEdgeByP1(pattern));
        undirectedEdges.addAll(this.findUndirectedEdgeByP2(pattern));
        if (0 == undirectedEdges.size()) {
            throw new DirectedEdgeNotFoundException(String.format("No DirectedEdge found with Pattern %s as target", pattern.getId()));
        }
        return undirectedEdges;
    }

    @Override
    @Transactional
    public UndirectedEdge updateUndirectedEdge(UndirectedEdge undirectedEdge) {
        return this.undirectedEdgeReository.save(undirectedEdge);
    }

    @Override
    @Transactional
    public void deleteUndirectedEdgeById(UUID undirectedEdgeId) {
        UndirectedEdge undirectedEdge = this.getUndirectedEdgeById(undirectedEdgeId);
        this.deleteUndirectedEdge(undirectedEdge);
    }

    @Override
    @Transactional
    public void deleteUndirectedEdge(UndirectedEdge undirectedEdge) {
        undirectedEdge.setPatternViews(null);
        this.updateUndirectedEdge(undirectedEdge);
        this.undirectedEdgeReository.delete(undirectedEdge);
    }

    @Override
    @Transactional
    public void deleteAllUndirectedEdges(Iterable<UndirectedEdge> undirectedEdges) {
        for (UndirectedEdge undirectedEdge : undirectedEdges) {
            this.deleteUndirectedEdge(undirectedEdge);
        }
    }

    private List<UndirectedEdge> findUndirectedEdgeByP1(Pattern pattern) {
        return this.undirectedEdgeReository.findByP1(pattern)
                .orElse(Collections.emptyList());
    }

    private List<UndirectedEdge> findUndirectedEdgeByP2(Pattern pattern) {
        return this.undirectedEdgeReository.findByP2(pattern)
                .orElse(Collections.emptyList());
    }
}
