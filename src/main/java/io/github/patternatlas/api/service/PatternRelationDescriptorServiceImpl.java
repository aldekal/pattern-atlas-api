package io.github.patternatlas.api.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.patternatlas.api.entities.DirectedEdge;
import io.github.patternatlas.api.entities.Pattern;
import io.github.patternatlas.api.entities.UndirectedEdge;
import io.github.patternatlas.api.exception.DirectedEdgeNotFoundException;
import io.github.patternatlas.api.exception.UndirectedEdgeNotFoundException;
import io.github.patternatlas.api.repositories.DirectedEdgeRepository;
import io.github.patternatlas.api.repositories.UndirectedEdgeReository;

@Service
public class PatternRelationDescriptorServiceImpl implements PatternRelationDescriptorService {

    private final DirectedEdgeRepository directedEdgeRepository;

    private final UndirectedEdgeReository undirectedEdgeReository;

    public PatternRelationDescriptorServiceImpl(DirectedEdgeRepository directedEdgeRepository,
                                                UndirectedEdgeReository undirectedEdgeReository) {
        this.directedEdgeRepository = directedEdgeRepository;
        this.undirectedEdgeReository = undirectedEdgeReository;
    }

    @Override
    @Transactional
    public DirectedEdge createDirectedEdge(DirectedEdge directedEdge) {
        return this.directedEdgeRepository.save(directedEdge);
    }

    @Override
    @Transactional(readOnly = true)
    public DirectedEdge getDirectedEdgeById(UUID id) throws DirectedEdgeNotFoundException {
        return this.directedEdgeRepository.findById(id)
                .orElseThrow(() -> new DirectedEdgeNotFoundException(String.format("DirectedEdge %s not found!", id.toString())));
    }

    @Override
    public List<DirectedEdge> findDirectedEdgeBySource(Pattern pattern) throws DirectedEdgeNotFoundException {
        return this.directedEdgeRepository.findBySource(pattern)
                .orElseThrow(() -> new DirectedEdgeNotFoundException(String.format("No DirectedEdge found with Pattern %s as source", pattern.getId())));
    }

    @Override
    public List<DirectedEdge> findDirectedEdgeByTarget(Pattern pattern) throws DirectedEdgeNotFoundException {
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
    public UndirectedEdge getUndirectedEdgeById(UUID id) throws UndirectedEdgeNotFoundException {
        return this.undirectedEdgeReository.findById(id)
                .orElseThrow(() -> new UndirectedEdgeNotFoundException(String.format("UndirectedEdge %s not found!", id.toString())));
    }

    @Override
    public List<UndirectedEdge> findUndirectedEdgeByPattern(Pattern pattern) throws UndirectedEdgeNotFoundException {
        List<UndirectedEdge> undirectedEdges = new ArrayList<>();
        undirectedEdges.addAll(this.findUndirectedEdgeByP1(pattern));
        undirectedEdges.addAll(this.findUndirectedEdgeByP2(pattern));
        if (0 == undirectedEdges.size()) {
            throw new UndirectedEdgeNotFoundException(String.format("No UndirectedEdge found with Pattern %s", pattern.getId()));
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
