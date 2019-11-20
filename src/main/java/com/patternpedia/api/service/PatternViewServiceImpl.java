package com.patternpedia.api.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.PatternView;
import com.patternpedia.api.entities.UndirectedEdge;
import com.patternpedia.api.exception.DirectedEdgeNotFoundException;
import com.patternpedia.api.exception.NullPatternViewException;
import com.patternpedia.api.exception.PatternNotFoundException;
import com.patternpedia.api.exception.PatternViewNotFoundException;
import com.patternpedia.api.exception.UndirectedEdgeNotFoundException;
import com.patternpedia.api.repositories.DirectedEdgeRepository;
import com.patternpedia.api.repositories.PatternViewRepository;
import com.patternpedia.api.repositories.UndirectedEdgeReository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PatternViewServiceImpl implements PatternViewService {

    private PatternService patternService;
    private PatternRelationDescriptorService patternRelationDescriptorService;
    private PatternViewRepository patternViewRepository;
    private DirectedEdgeRepository directedEdgeRepository;
    private UndirectedEdgeReository undirectedEdgeReository;

    public PatternViewServiceImpl(PatternService patternService,
                                  PatternRelationDescriptorService patternRelationDescriptorService,
                                  PatternViewRepository patternViewRepository,
                                  DirectedEdgeRepository directedEdgeRepository,
                                  UndirectedEdgeReository undirectedEdgeReository) {
        this.patternService = patternService;
        this.patternRelationDescriptorService = patternRelationDescriptorService;
        this.patternViewRepository = patternViewRepository;
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

        patternView.setPatterns(this.getPatternsOfPatternView(patternView.getId()));
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

    // Pattern Handling

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
    public List<Pattern> getPatternsOfPatternView(UUID patternViewId) {
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

    // DirectedEdge Handling

    @Override
    @Transactional
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
    @Transactional
    public DirectedEdge createDirectedEdgeAndAddToPatternView(UUID patternViewId, DirectedEdge directedEdge) {
        PatternView patternView = this.getPatternViewById(patternViewId);
        if (null != directedEdge.getPatternViews()) {
            directedEdge.getPatternViews().add(patternView);
        } else {
            directedEdge.setPatternViews(new ArrayList<>(Collections.singletonList(patternView)));
        }

        directedEdge = this.patternRelationDescriptorService.createDirectedEdge(directedEdge);
        if (null != patternView.getDirectedEdges()) {
            patternView.getDirectedEdges().add(directedEdge);
        } else {
            patternView.setDirectedEdges(new ArrayList<>(Collections.singletonList(directedEdge)));
        }
        this.patternViewRepository.save(patternView);
        return directedEdge;
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
    @Transactional(readOnly = true)
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
    @Transactional
    public DirectedEdge updateDirectedEdgeOfPatternView(UUID patternViewId, DirectedEdge directedEdge) {
        if (null == directedEdge.getId()) {
            // since the edge has no UUID we assume that it is a new edge and try to persist it
            return this.createDirectedEdgeAndAddToPatternView(patternViewId, directedEdge);
        }

        PatternView patternView = this.getPatternViewById(patternViewId);
        if (null != patternView.getDirectedEdges()) {
            if (patternView.getDirectedEdges().contains(directedEdge)) {
                DirectedEdge persisted = this.patternRelationDescriptorService.getDirectedEdgeById(directedEdge.getId());
                directedEdge.setPatternLanguage(persisted.getPatternLanguage());
                directedEdge.setPatternViews(persisted.getPatternViews());
                return this.patternRelationDescriptorService.updateDirectedEdge(directedEdge);
            } else {
                throw new DirectedEdgeNotFoundException(patternView, directedEdge.getId());
            }
        } else {
            throw new DirectedEdgeNotFoundException(patternView, directedEdge.getId());
        }
    }

    @Override
    @Transactional
    public void removeDirectedEdgeFromPatternView(UUID patternViewId, UUID directedEdgeId) {
        DirectedEdge directedEdge = this.patternRelationDescriptorService.getDirectedEdgeById(directedEdgeId);
        if (null != directedEdge.getPatternLanguage()) {
            // DirectedEdge is part of pattern language, thus we do not remove it from database but drop it from view
            PatternView patternView = this.getPatternViewById(patternViewId);
            if (null != patternView.getDirectedEdges()) {
                patternView.getDirectedEdges().remove(directedEdge);
                this.patternViewRepository.save(patternView);
            }
        } else {
            // DirectedEdge is not part of pattern language, thus we remove it from database
            List<PatternView> patternViews = directedEdge.getPatternViews();
            if (null == patternViews) {
                this.patternRelationDescriptorService.deleteDirectedEdgeById(directedEdgeId);
            } else {
                for (PatternView patternView : patternViews) {
                    if (null != patternView.getDirectedEdges()) {
                        patternView.getDirectedEdges().remove(directedEdge);
                        this.patternViewRepository.save(patternView);
                    }
                }
                this.patternRelationDescriptorService.deleteDirectedEdgeById(directedEdgeId);
            }
        }
    }

    // UndirectedEdge Handling

    @Override
    @Transactional
    public void addUndirectedEdgeToPatternView(UUID patternViewId, UUID undirectedEdgeId) {
        PatternView patternView = this.getPatternViewById(patternViewId);
        UndirectedEdge edge = this.patternRelationDescriptorService.getUndirectedEdgeById(undirectedEdgeId);

        if (null == patternView.getUndirectedEdges()) {
            patternView.setUndirectedEdges(new ArrayList<>(Collections.singletonList(edge)));
        } else {
            patternView.getUndirectedEdges().add(edge);
        }

        this.patternViewRepository.save(patternView);
    }

    @Override
    @Transactional
    public UndirectedEdge createUndirectedEdgeAndAddToPatternView(UUID patternViewId, UndirectedEdge undirectedEdge) {
        PatternView patternView = this.getPatternViewById(patternViewId);
        if (null != undirectedEdge.getPatternViews()) {
            undirectedEdge.getPatternViews().add(patternView);
        } else {
            undirectedEdge.setPatternViews(new ArrayList<>(Collections.singletonList(patternView)));
        }

        undirectedEdge = this.patternRelationDescriptorService.createUndirectedEdge(undirectedEdge);
        if (null != patternView.getUndirectedEdges()) {
            patternView.getUndirectedEdges().add(undirectedEdge);
        } else {
            patternView.setUndirectedEdges(new ArrayList<>(Collections.singletonList(undirectedEdge)));
        }
        this.patternViewRepository.save(patternView);
        return undirectedEdge;
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
    @Transactional(readOnly = true)
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
    @Transactional
    public UndirectedEdge updateUndirectedEdgeOfPatternView(UUID patternViewId, UndirectedEdge undirectedEdge) {
        if (null == undirectedEdge.getId()) {
            // since the edge has no UUID we assume that it is a new edge and try to persist it
            return this.createUndirectedEdgeAndAddToPatternView(patternViewId, undirectedEdge);
        }

        PatternView patternView = this.getPatternViewById(patternViewId);
        if (null != patternView.getUndirectedEdges()) {
            if (patternView.getUndirectedEdges().contains(undirectedEdge)) {
                UndirectedEdge persisted = this.patternRelationDescriptorService.getUndirectedEdgeById(undirectedEdge.getId());
                undirectedEdge.setPatternLanguage(persisted.getPatternLanguage());
                undirectedEdge.setPatternViews(persisted.getPatternViews());
                return this.patternRelationDescriptorService.updateUndirectedEdge(undirectedEdge);
            } else {
                throw new UndirectedEdgeNotFoundException(patternView, undirectedEdge.getId());
            }
        } else {
            throw new UndirectedEdgeNotFoundException(patternView, undirectedEdge.getId());
        }
    }

    @Override
    @Transactional
    public void removeUndirectedEdgeFromPatternView(UUID patternViewId, UUID undirectedEdgeId) {
        UndirectedEdge edge = this.patternRelationDescriptorService.getUndirectedEdgeById(undirectedEdgeId);
        if (null != edge.getPatternLanguage()) {
            // DirectedEdge is part of pattern language, thus we do not remove it from database but drop it from view
            PatternView patternView = this.getPatternViewById(patternViewId);
            if (null != patternView.getUndirectedEdges()) {
                patternView.getUndirectedEdges().remove(edge);
                this.patternViewRepository.save(patternView);
            }
        } else {
            // DirectedEdge is not part of pattern language, thus we remove it from database
            List<PatternView> patternViews = edge.getPatternViews();
            if (null == patternViews) {
                this.patternRelationDescriptorService.deleteUndirectedEdgeById(undirectedEdgeId);
            } else {
                for (PatternView patternView : patternViews) {
                    if (null != patternView.getUndirectedEdges()) {
                        patternView.getUndirectedEdges().remove(edge);
                        this.patternViewRepository.save(patternView);
                    }
                }
                this.patternRelationDescriptorService.deleteUndirectedEdgeById(undirectedEdgeId);
            }
        }
    }
}
