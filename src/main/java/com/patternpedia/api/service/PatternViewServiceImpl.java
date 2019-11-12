package com.patternpedia.api.service;

import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternView;
import com.patternpedia.api.entities.UndirectedEdge;
import com.patternpedia.api.exception.*;
import com.patternpedia.api.repositories.DirectedEdgeRepository;
import com.patternpedia.api.repositories.PatternViewRepository;
import com.patternpedia.api.repositories.UndirectedEdgeReository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PatternViewServiceImpl implements PatternViewService {

    private PatternViewRepository patternViewRepository;
    private PatternService patternService;
    private PatternRelationDescriptorService patternRelationDescriptorService;
    private DirectedEdgeRepository directedEdgeRepository;
    private UndirectedEdgeReository undirectedEdgeReository;

    public PatternViewServiceImpl(PatternViewRepository patternViewRepository,
                                  PatternService patternService,
                                  DirectedEdgeRepository directedEdgeRepository,
                                  UndirectedEdgeReository undirectedEdgeReository,
                                  PatternRelationDescriptorService patternRelationDescriptorService) {
        this.patternViewRepository = patternViewRepository;
        this.patternService = patternService;
        this.directedEdgeRepository = directedEdgeRepository;
        this.undirectedEdgeReository = undirectedEdgeReository;
        this.patternRelationDescriptorService = patternRelationDescriptorService;
    }

    @Override
    @Transactional
    public PatternView createPatternView(PatternView patternView) {
        if (null == patternView) {
            throw new NullPatternViewException();
        }

        return this.patternViewRepository.save(patternView);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatternView> getAllPatternViews() {
        return this.patternViewRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PatternView getPatternViewById(UUID patternViewId) {
        return this.patternViewRepository.findById(patternViewId)
                .orElseThrow(() -> new PatternViewNotFoundException(patternViewId));
    }

    @Override
    @Transactional(readOnly = true)
    public PatternView getPatternViewByUri(String uri) {
        return this.patternViewRepository.findByUri(uri)
                .orElseThrow(() -> new PatternViewNotFoundException(String.format("PatternView with URI \"%s\" not found!", uri)));
    }

    @Override
    @Transactional
    public PatternView updatePatternView(PatternView patternView) {
        // We just support updating fields of PatternView but we don't support updates of sub resources such as pattern edges.
        // So we ignore patterns and edges.
        if (null == patternView) {
            throw new NullPatternViewException();
        }

        if (!this.patternViewRepository.existsById(patternView.getId())) {
            throw new PatternViewNotFoundException(patternView);
        }

        patternView.setPatterns(this.getAllPatternsOfPatternView(patternView.getId()));
        patternView.setDirectedEdges(this.getDirectedEdgesByPatternViewId(patternView.getId()));
        patternView.setUndirectedEdges(this.getUndirectedEdgesByPatternViewId(patternView.getId()));

        return this.patternViewRepository.save(patternView);
    }

    @Override
    @Transactional
    public void deletePatternView(UUID patternViewId) {
        PatternView patternView = this.getPatternViewById(patternViewId);

        // Remove edges that are just part of pattern view
        if (null != patternView.getDirectedEdges()) {
            patternView.getDirectedEdges().forEach(directedEdge -> {
                if (null == directedEdge.getPatternLanguage()) {
                    this.directedEdgeRepository.delete(directedEdge);
                }
            });
        }

        if (null != patternView.getUndirectedEdges()) {
            patternView.getUndirectedEdges().forEach(undirectedEdge -> {
                if (null == undirectedEdge.getPatternLanguage()) {
                    this.undirectedEdgeReository.delete(undirectedEdge);
                }
            });
        }

        this.patternViewRepository.delete(patternView);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pattern> getAllPatternsOfPatternView(UUID patternViewId) {
        PatternView patternView = this.getPatternViewById(patternViewId);
        if (null == patternView.getPatterns()) {
            patternView.setPatterns(Collections.emptyList());
        }
        return patternView.getPatterns();
    }

    @Override
    @Transactional(readOnly = true)
    public Pattern getPatternOfPatternViewById(UUID patternViewId, UUID patternId) {
        PatternView patternView = this.getPatternViewById(patternViewId);
        if (null == patternView.getPatterns()) {
            throw new PatternNotFoundException(patternView, patternId);
        }
        return patternView.getPatterns()
                .stream()
                .filter(pattern -> pattern.getId().equals(patternId)).findFirst()
                .orElseThrow(() -> new PatternNotFoundException(patternView, patternId));
    }

    @Override
    @Transactional
    public void removePatternFromPatternView(UUID patternViewId, UUID patternId) {
        PatternView patternView = this.getPatternViewById(patternViewId);
        Pattern pattern = this.getPatternOfPatternViewById(patternViewId, patternId);

        if (null == patternView.getPatterns() || !patternView.getPatterns().contains(pattern)) {
            throw new PatternNotFoundException(patternView, patternId);
        } else {
            patternView.getPatterns().remove(pattern);
            this.patternViewRepository.save(patternView);
        }
    }

    @Override
    @Transactional
    public void addPatternToPatternView(UUID patternViewId, UUID patternId) {
        PatternView patternView = this.getPatternViewById(patternViewId);
        Pattern pattern = this.patternService.getPatternById(patternId);

        if (null == patternView.getPatterns()) {
            patternView.setPatterns(new ArrayList<>(Collections.singletonList(pattern)));
        } else if (!patternView.getPatterns().contains(pattern)) {
            patternView.getPatterns().add(pattern);
        }

        this.patternViewRepository.save(patternView);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DirectedEdge> getDirectedEdgesByPatternViewId(UUID patternViewId) {
        PatternView patternView = this.getPatternViewById(patternViewId);
        if (null == patternView.getDirectedEdges()) {
            patternView.setDirectedEdges(Collections.emptyList());
        }
        return patternView.getDirectedEdges();
    }

    @Override
    public DirectedEdge getDirectedEdgeOfPatternViewById(UUID patternViewId, UUID directedEdgeId) {
        PatternView patternView = this.getPatternViewById(patternViewId);

        if (null == patternView.getDirectedEdges()) {
            throw new DirectedEdgeNotFoundException(patternView, directedEdgeId);
        }

        return patternView.getDirectedEdges()
                .stream()
                .filter(directedEdge -> directedEdge.getId().equals(directedEdgeId)).findFirst()
                .orElseThrow(() -> new DirectedEdgeNotFoundException(patternView, directedEdgeId));
    }

    @Override
    public DirectedEdge createDirectedEdgeAndAddToPatternView(UUID patternViewId, DirectedEdge directedEdge) {
        PatternView patternView = this.getPatternViewById(patternViewId);

        if (null != directedEdge.getId() && this.directedEdgeRepository.existsById(directedEdge.getId())) {
            throw new RuntimeException("DirectedEdge already present in database!");
        }

        directedEdge.setPatternViews(new ArrayList<>(Collections.singletonList(patternView)));
        directedEdge = this.directedEdgeRepository.save(directedEdge);

        if (null == patternView.getDirectedEdges()) {
            patternView.setDirectedEdges(new ArrayList<>(Collections.singletonList(directedEdge)));
        } else {
            patternView.getDirectedEdges().add(directedEdge);
        }

        this.patternViewRepository.save(patternView);

        return directedEdge;
    }

    @Override
    public void addDirectedEdgeToPatternView(UUID patternViewId, UUID directedEdgeId) {
        PatternView patternView = this.getPatternViewById(patternViewId);
        DirectedEdge directedEdge = this.patternRelationDescriptorService.getDirectedEdgeById(directedEdgeId);

        if (null == patternView.getDirectedEdges()) {
            patternView.setDirectedEdges(new ArrayList<>(Collections.singletonList(directedEdge)));
        } else {
            patternView.getDirectedEdges().add(directedEdge);
        }

        this.patternViewRepository.save(patternView);
    }

    @Override
    public void removeDirectedEdgeFromPatternView(UUID patternViewId, UUID directedEdgeId) {
        // TODO Go on here...
        throw new NotImplementedException();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UndirectedEdge> getUndirectedEdgesByPatternViewId(UUID patternViewId) {
        PatternView patternView = this.getPatternViewById(patternViewId);
        if (null == patternView.getUndirectedEdges()) {
            patternView.setUndirectedEdges(Collections.emptyList());
        }
        return patternView.getUndirectedEdges();
    }

    @Override
    public UndirectedEdge getUndirectedEdgeOfPatternViewById(UUID patternViewId, UUID undirectedEdgeId) {
        PatternView patternView = this.getPatternViewById(patternViewId);

        if (null == patternView.getUndirectedEdges()) {
            throw new UndirectedEdgeNotFoundException(patternView, undirectedEdgeId);
        }

        return patternView.getUndirectedEdges().stream()
                .filter(undirectedEdge -> undirectedEdge.getId().equals(undirectedEdgeId)).findFirst()
                .orElseThrow(() -> new UndirectedEdgeNotFoundException(patternView, undirectedEdgeId));
    }

    @Override
    public UndirectedEdge createUndirectedEdgeAndAddToPatternView(UUID patternViewId, UndirectedEdge undirectedEdge) {
        // TODO Go on here...
        throw new NotImplementedException();
    }

    @Override
    public UndirectedEdge addUndirectedEdgeToPatternView(UUID patternViewId, UUID undirectedEdgeId) {
        // TODO Go on here...
        throw new NotImplementedException();
    }

    @Override
    public void removeUndirectedEdgeFromPatternView(UUID patternViewId, UUID undirectedEdgeId) {
        // TODO Go on here...
        throw new NotImplementedException();
    }
}
