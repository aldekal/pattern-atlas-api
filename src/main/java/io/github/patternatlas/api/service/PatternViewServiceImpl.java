package io.github.patternatlas.api.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.patternatlas.api.entities.DirectedEdge;
import io.github.patternatlas.api.entities.Pattern;
import io.github.patternatlas.api.entities.PatternGraphType;
import io.github.patternatlas.api.entities.PatternView;
import io.github.patternatlas.api.entities.PatternViewDirectedEdge;
import io.github.patternatlas.api.entities.PatternViewDirectedEdgeId;
import io.github.patternatlas.api.entities.PatternViewPattern;
import io.github.patternatlas.api.entities.PatternViewPatternId;
import io.github.patternatlas.api.entities.PatternViewUndirectedEdge;
import io.github.patternatlas.api.entities.PatternViewUndirectedEdgeId;
import io.github.patternatlas.api.entities.UndirectedEdge;
import io.github.patternatlas.api.exception.DirectedEdgeNotFoundException;
import io.github.patternatlas.api.exception.NullPatternViewException;
import io.github.patternatlas.api.exception.PatternNotFoundException;
import io.github.patternatlas.api.exception.PatternViewNotFoundException;
import io.github.patternatlas.api.exception.UndirectedEdgeNotFoundException;
import io.github.patternatlas.api.repositories.DirectedEdgeRepository;
import io.github.patternatlas.api.repositories.PatternViewDirectedEdgeRepository;
import io.github.patternatlas.api.repositories.PatternViewPatternRepository;
import io.github.patternatlas.api.repositories.PatternViewRepository;
import io.github.patternatlas.api.repositories.PatternViewUndirectedEdgeRepository;
import io.github.patternatlas.api.repositories.UndirectedEdgeReository;
import io.github.patternatlas.api.rest.model.AddDirectedEdgeToViewRequest;
import io.github.patternatlas.api.rest.model.AddUndirectedEdgeToViewRequest;
import io.github.patternatlas.api.rest.model.UpdateDirectedEdgeRequest;
import io.github.patternatlas.api.rest.model.UpdateUndirectedEdgeRequest;

@Service
@Transactional
public class PatternViewServiceImpl implements PatternViewService {

    private final PatternService patternService;
    private final PatternRelationDescriptorService patternRelationDescriptorService;
    private final PatternViewRepository patternViewRepository;
    private final PatternViewPatternRepository patternViewPatternRepository;
    private final PatternViewDirectedEdgeRepository patternViewDirectedEdgeRepository;
    private final PatternViewUndirectedEdgeRepository patternViewUndirectedEdgeRepository;
    private final DirectedEdgeRepository directedEdgeRepository;
    private final UndirectedEdgeReository undirectedEdgeReository;

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
    public DirectedEdge createDirectedEdgeAndAddToPatternView(UUID patternViewId, AddDirectedEdgeToViewRequest request) {
        PatternView patternView = this.getPatternViewById(patternViewId);

        DirectedEdge directedEdge = new DirectedEdge();
        directedEdge.setSource(this.patternService.getPatternById(request.getSourcePatternId()));
        directedEdge.setTarget(this.patternService.getPatternById(request.getTargetPatternId()));
        directedEdge.setDescription(request.getDescription());
        directedEdge.setType(request.getType());
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
    public DirectedEdge updateDirectedEdgeOfPatternView(UUID patternViewId, UpdateDirectedEdgeRequest request) {

        PatternViewDirectedEdge patternViewDirectedEdge = this.patternViewDirectedEdgeRepository
                .findById(new PatternViewDirectedEdgeId(patternViewId, request.getDirectedEdgeId()))
                .orElseThrow(() -> new DirectedEdgeNotFoundException(patternViewId, request.getDirectedEdgeId(), PatternGraphType.PATTERN_VIEW));

        DirectedEdge directedEdge = patternViewDirectedEdge.getDirectedEdge();
        directedEdge.setType(request.getType());
        directedEdge.setDescription(request.getDescription());

        Pattern source = this.patternService.getPatternById(request.getSourcePatternId());
        directedEdge.setSource(source);

        Pattern target = this.patternService.getPatternById(request.getTargetPatternId());
        directedEdge.setTarget(target);

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
    public UndirectedEdge createUndirectedEdgeAndAddToPatternView(UUID patternViewId, AddUndirectedEdgeToViewRequest request) {
        PatternView patternView = this.getPatternViewById(patternViewId);

        UndirectedEdge undirectedEdge = new UndirectedEdge();
        undirectedEdge.setP1(this.patternService.getPatternById(request.getPattern1Id()));
        undirectedEdge.setP2(this.patternService.getPatternById(request.getPattern2Id()));
        undirectedEdge.setDescription(request.getDescription());
        undirectedEdge.setType(request.getType());
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
    public UndirectedEdge updateUndirectedEdgeOfPatternView(UUID patternViewId, UpdateUndirectedEdgeRequest request) {
        PatternViewUndirectedEdge edge = this.patternViewUndirectedEdgeRepository
                .findById(new PatternViewUndirectedEdgeId(patternViewId, request.getUndirectedEdgeId()))
                .orElseThrow(() -> new UndirectedEdgeNotFoundException(patternViewId, request.getUndirectedEdgeId(), PatternGraphType.PATTERN_VIEW));

        UndirectedEdge undirectedEdge = edge.getUndirectedEdge();
        undirectedEdge.setType(request.getType());
        undirectedEdge.setDescription(request.getDescription());

        Pattern p1 = this.patternService.getPatternById(request.getPattern1Id());
        undirectedEdge.setP1(p1);

        Pattern p2 = this.patternService.getPatternById(request.getPattern2Id());
        undirectedEdge.setP2(p2);

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

    @Override
    @Transactional(readOnly = true)
    public Object getGraphOfPatternView(UUID patternViewId) {
        PatternView patternView = this.getPatternViewById(patternViewId);
        return patternView.getGraph();
    }

    @Override
    @Transactional
    public Object createGraphOfPatternView(UUID patternViewId, Object graph) {
        PatternView patternView = this.getPatternViewById(patternViewId);
        patternView.setGraph(graph);
        patternView = this.updatePatternView(patternView);
        return patternView.getGraph();
    }

    @Override
    public Object updateGraphOfPatternView(UUID patternViewId, Object graph) {
        PatternView patternView = this.getPatternViewById(patternViewId);
        patternView.setGraph(graph);
        patternView = this.updatePatternView(patternView);
        return patternView.getGraph();
    }

    @Override
    @Transactional
    public void deleteGraphOfPatternView(UUID patternViewId) {
        PatternView patternView = this.getPatternViewById(patternViewId);
        patternView.setGraph(null);
        this.patternViewRepository.save(patternView);
    }
}
