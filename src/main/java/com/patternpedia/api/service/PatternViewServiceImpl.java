package com.patternpedia.api.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternGraphType;
import com.patternpedia.api.entities.PatternView;
import com.patternpedia.api.entities.PatternViewDirectedEdge;
import com.patternpedia.api.entities.PatternViewDirectedEdgeId;
import com.patternpedia.api.entities.PatternViewPattern;
import com.patternpedia.api.entities.PatternViewPatternId;
import com.patternpedia.api.entities.PatternViewUndirectedEdge;
import com.patternpedia.api.entities.PatternViewUndirectedEdgeId;
import com.patternpedia.api.entities.UndirectedEdge;
import com.patternpedia.api.exception.DirectedEdgeNotFoundException;
import com.patternpedia.api.exception.NullPatternViewException;
import com.patternpedia.api.exception.PatternNotFoundException;
import com.patternpedia.api.exception.PatternViewNotFoundException;
import com.patternpedia.api.exception.UndirectedEdgeNotFoundException;
import com.patternpedia.api.repositories.DirectedEdgeRepository;
import com.patternpedia.api.repositories.PatternViewDirectedEdgeRepository;
import com.patternpedia.api.repositories.PatternViewPatternRepository;
import com.patternpedia.api.repositories.PatternViewRepository;
import com.patternpedia.api.repositories.PatternViewUndirectedEdgeRepository;
import com.patternpedia.api.repositories.UndirectedEdgeReository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PatternViewServiceImpl implements PatternViewService {

    private PatternService patternService;
    private PatternRelationDescriptorService patternRelationDescriptorService;
    private PatternViewRepository patternViewRepository;
    private PatternViewPatternRepository patternViewPatternRepository;
    private PatternViewDirectedEdgeRepository patternViewDirectedEdgeRepository;
    private PatternViewUndirectedEdgeRepository patternViewUndirectedEdgeRepository;
    private DirectedEdgeRepository directedEdgeRepository;
    private UndirectedEdgeReository undirectedEdgeReository;

    public PatternViewServiceImpl(PatternService patternService,
                                  PatternRelationDescriptorService patternRelationDescriptorService,
                                  PatternViewRepository patternViewRepository,
                                  PatternViewPatternRepository patternViewPatternRepository,
                                  PatternViewDirectedEdgeRepository patternViewDirectedEdgeRepository,
                                  PatternViewUndirectedEdgeRepository patternViewUndirectedEdgeRepository,
                                  DirectedEdgeRepository directedEdgeRepository,
                                  UndirectedEdgeReository undirectedEdgeReository) {
        this.patternService = patternService;
        this.patternRelationDescriptorService = patternRelationDescriptorService;
        this.patternViewRepository = patternViewRepository;
        this.patternViewPatternRepository = patternViewPatternRepository;
        this.patternViewDirectedEdgeRepository = patternViewDirectedEdgeRepository;
        this.patternViewUndirectedEdgeRepository = patternViewUndirectedEdgeRepository;
        this.directedEdgeRepository = directedEdgeRepository;
        this.undirectedEdgeReository = undirectedEdgeReository;
    }

    // PatternView Handling

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

        PatternView oldPatternView = this.getPatternViewById(patternView.getId());

        patternView.setPatterns(oldPatternView.getPatterns());
        patternView.setDirectedEdges(oldPatternView.getDirectedEdges());
        patternView.setUndirectedEdges(oldPatternView.getUndirectedEdges());

        return this.patternViewRepository.save(patternView);
    }

    @Override
    @Transactional
    public void deletePatternView(UUID patternViewId) {
        PatternView patternView = this.getPatternViewById(patternViewId);

        // Remove edges that are just part of pattern view
        if (null != patternView.getDirectedEdges()) {
            patternView.getDirectedEdges().forEach(patternViewDirectedEdge -> {
                if (null == patternViewDirectedEdge.getDirectedEdge().getPatternLanguage()) {
                    this.directedEdgeRepository.delete(patternViewDirectedEdge.getDirectedEdge());
                }
            });
        }

        if (null != patternView.getUndirectedEdges()) {
            patternView.getUndirectedEdges().forEach(patternViewUndirectedEdge -> {
                if (null == patternViewUndirectedEdge.getUndirectedEdge().getPatternLanguage()) {
                    this.undirectedEdgeReository.delete(patternViewUndirectedEdge.getUndirectedEdge());
                }
            });
        }

        this.patternViewRepository.delete(patternView);
    }

    // Pattern Handling

    @Override
    @Transactional
    public void addPatternToPatternView(UUID patternViewId, UUID patternId) {
        PatternView patternView = this.getPatternViewById(patternViewId);
        Pattern pattern = this.patternService.getPatternById(patternId);

        PatternViewPattern patternViewPattern = new PatternViewPattern(patternView, pattern);
        this.patternViewPatternRepository.save(patternViewPattern);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pattern> getPatternsOfPatternView(UUID patternViewId) {
        return this.patternViewPatternRepository.findAllByPatternViewId(patternViewId).stream()
                .map(PatternViewPattern::getPattern)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Pattern getPatternOfPatternViewById(UUID patternViewId, UUID patternId) {
        PatternViewPattern patternViewPattern = this.patternViewPatternRepository.findById(new PatternViewPatternId(patternViewId, patternId))
                .orElseThrow(() -> new PatternNotFoundException(patternViewId, patternId, PatternGraphType.PATTERN_VIEW));

        return patternViewPattern.getPattern();
    }

    @Override
    @Transactional
    public void removePatternFromPatternView(UUID patternViewId, UUID patternId) {
        PatternViewPatternId id = new PatternViewPatternId(patternViewId, patternId);
        if (this.patternViewPatternRepository.existsById(id)) {
            this.patternViewPatternRepository.deleteById(id);
        } else {
            throw new PatternNotFoundException(patternViewId, patternId, PatternGraphType.PATTERN_VIEW);
        }
    }

    // DirectedEdge Handling

    @Override
    @Transactional
    public void addDirectedEdgeToPatternView(UUID patternViewId, UUID directedEdgeId) {
        PatternView patternView = this.getPatternViewById(patternViewId);
        DirectedEdge directedEdge = this.patternRelationDescriptorService.getDirectedEdgeById(directedEdgeId);

        PatternViewDirectedEdge patternViewDirectedEdge = new PatternViewDirectedEdge(patternView, directedEdge);
        this.patternViewDirectedEdgeRepository.save(patternViewDirectedEdge);
    }

    @Override
    @Transactional
    public DirectedEdge createDirectedEdgeAndAddToPatternView(UUID patternViewId, DirectedEdge directedEdge) {
        PatternView patternView = this.getPatternViewById(patternViewId);
        directedEdge = this.patternRelationDescriptorService.createDirectedEdge(directedEdge);

        PatternViewDirectedEdge patternViewDirectedEdge = new PatternViewDirectedEdge(patternView, directedEdge);
        patternViewDirectedEdge = this.patternViewDirectedEdgeRepository.save(patternViewDirectedEdge);

        return patternViewDirectedEdge.getDirectedEdge();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DirectedEdge> getDirectedEdgesByPatternViewId(UUID patternViewId) {
        return this.patternViewDirectedEdgeRepository.findByPatternViewId(patternViewId).stream()
                .map(PatternViewDirectedEdge::getDirectedEdge)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DirectedEdge getDirectedEdgeOfPatternViewById(UUID patternViewId, UUID directedEdgeId) {
        return this.patternViewDirectedEdgeRepository.findById(new PatternViewDirectedEdgeId(patternViewId, directedEdgeId))
                .map(PatternViewDirectedEdge::getDirectedEdge)
                .orElseThrow(() -> new DirectedEdgeNotFoundException(patternViewId, directedEdgeId, PatternGraphType.PATTERN_VIEW));
    }

    @Override
    @Transactional
    public DirectedEdge updateDirectedEdgeOfPatternView(UUID patternViewId, DirectedEdge directedEdge) {
        if (null == directedEdge.getId()) {
            // since the edge has no UUID we assume that it is a new edge and try to persist it
            return this.createDirectedEdgeAndAddToPatternView(patternViewId, directedEdge);
        }

        PatternViewDirectedEdge patternViewDirectedEdge = this.patternViewDirectedEdgeRepository
                .findById(new PatternViewDirectedEdgeId(patternViewId, directedEdge.getId()))
                .orElseThrow(() -> new DirectedEdgeNotFoundException(patternViewId, directedEdge.getId(), PatternGraphType.PATTERN_VIEW));

        DirectedEdge persisted = patternViewDirectedEdge.getDirectedEdge();
        directedEdge.setPatternLanguage(persisted.getPatternLanguage());
        directedEdge.setPatternViews(persisted.getPatternViews());

        return this.patternRelationDescriptorService.updateDirectedEdge(directedEdge);
    }

    @Override
    @Transactional
    public void removeDirectedEdgeFromPatternView(UUID patternViewId, UUID directedEdgeId) throws DirectedEdgeNotFoundException {
        DirectedEdge directedEdge = this.patternRelationDescriptorService.getDirectedEdgeById(directedEdgeId);
        PatternView patternView = this.getPatternViewById(patternViewId);
        PatternViewDirectedEdge patternViewDirectedEdge = this.patternViewDirectedEdgeRepository.getOne(new PatternViewDirectedEdgeId(patternViewId, directedEdgeId));

        if (null != patternViewDirectedEdge) {
            this.patternViewDirectedEdgeRepository.delete(patternViewDirectedEdge);
        } else {
            throw new DirectedEdgeNotFoundException(patternView, directedEdgeId);
        }

        if (null == directedEdge.getPatternLanguage()) {
            // directed edge is not part of pattern language, thus remove it if it is not part of other views
            if (!this.patternViewDirectedEdgeRepository.existsByDirectedEdgeId(directedEdgeId)) {
                this.directedEdgeRepository.delete(directedEdge);
            }
        }
    }

    // UndirectedEdge Handling

    @Override
    @Transactional
    public void addUndirectedEdgeToPatternView(UUID patternViewId, UUID undirectedEdgeId) {
        PatternView patternView = this.getPatternViewById(patternViewId);
        UndirectedEdge undirectedEdge = this.patternRelationDescriptorService.getUndirectedEdgeById(undirectedEdgeId);

        PatternViewUndirectedEdge patternViewUndirectedEdge = new PatternViewUndirectedEdge(patternView, undirectedEdge);
        this.patternViewUndirectedEdgeRepository.save(patternViewUndirectedEdge);
    }

    @Override
    @Transactional
    public UndirectedEdge createUndirectedEdgeAndAddToPatternView(UUID patternViewId, UndirectedEdge undirectedEdge) {
        PatternView patternView = this.getPatternViewById(patternViewId);
        undirectedEdge = this.patternRelationDescriptorService.createUndirectedEdge(undirectedEdge);

        PatternViewUndirectedEdge patternViewUndirectedEdge = new PatternViewUndirectedEdge(patternView, undirectedEdge);
        patternViewUndirectedEdge = this.patternViewUndirectedEdgeRepository.save(patternViewUndirectedEdge);

        return patternViewUndirectedEdge.getUndirectedEdge();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UndirectedEdge> getUndirectedEdgesByPatternViewId(UUID patternViewId) {
        return this.patternViewUndirectedEdgeRepository.findByPatternViewId(patternViewId).stream()
                .map(PatternViewUndirectedEdge::getUndirectedEdge)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UndirectedEdge getUndirectedEdgeOfPatternViewById(UUID patternViewId, UUID undirectedEdgeId) {
        return this.patternViewUndirectedEdgeRepository.findById(new PatternViewUndirectedEdgeId(patternViewId, undirectedEdgeId))
                .map(PatternViewUndirectedEdge::getUndirectedEdge)
                .orElseThrow(() -> new UndirectedEdgeNotFoundException(patternViewId, undirectedEdgeId, PatternGraphType.PATTERN_VIEW));
    }

    @Override
    @Transactional
    public UndirectedEdge updateUndirectedEdgeOfPatternView(UUID patternViewId, UndirectedEdge undirectedEdge) {
        if (null == undirectedEdge.getId()) {
            // since the edge has no UUID we assume that it is a new edge and try to persist it
            return this.createUndirectedEdgeAndAddToPatternView(patternViewId, undirectedEdge);
        }

        PatternViewUndirectedEdge patternViewUndirectedEdge = this.patternViewUndirectedEdgeRepository
                .findById(new PatternViewUndirectedEdgeId(patternViewId, undirectedEdge.getId()))
                .orElseThrow(() -> new UndirectedEdgeNotFoundException(patternViewId, undirectedEdge.getId(), PatternGraphType.PATTERN_VIEW));

        UndirectedEdge persisted = patternViewUndirectedEdge.getUndirectedEdge();
        undirectedEdge.setPatternLanguage(persisted.getPatternLanguage());
        undirectedEdge.setPatternViews(persisted.getPatternViews());

        return this.patternRelationDescriptorService.updateUndirectedEdge(undirectedEdge);
    }

    @Override
    @Transactional
    public void removeUndirectedEdgeFromPatternView(UUID patternViewId, UUID undirectedEdgeId) {
        UndirectedEdge undirectedEdge = this.patternRelationDescriptorService.getUndirectedEdgeById(undirectedEdgeId);
        PatternView patternView = this.getPatternViewById(patternViewId);
        PatternViewUndirectedEdge patternViewUndirectedEdge = this.patternViewUndirectedEdgeRepository
                .getOne(new PatternViewUndirectedEdgeId(patternViewId, undirectedEdgeId));

        if (null != patternViewUndirectedEdge) {
            this.patternViewUndirectedEdgeRepository.delete(patternViewUndirectedEdge);
        } else {
            throw new UndirectedEdgeNotFoundException(patternView, undirectedEdgeId);
        }

        if (null == undirectedEdge.getPatternLanguage()) {
            // directed edge is not part of pattern language, thus remove it if it is not part of other views
            if (!this.patternViewUndirectedEdgeRepository.existsByUndirectedEdgeId(undirectedEdgeId)) {
                this.undirectedEdgeReository.delete(undirectedEdge);
            }
        }
    }
}
