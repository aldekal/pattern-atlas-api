package com.patternpedia.api.service;

import com.patternpedia.api.entities.Pattern;
import com.patternpedia.api.entities.designmodel.DesignModel;
import com.patternpedia.api.entities.designmodel.DesignModelPatternInstance;
import com.patternpedia.api.exception.DesignModelNotFoundException;
import com.patternpedia.api.exception.NullDesignModelException;
import com.patternpedia.api.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DesignModelServiceImpl implements DesignModelService {

    private PatternService patternService;
    //    private PatternRelationDescriptorService patternRelationDescriptorService;
    private DesignModelRepository designModelRepository;
    private DesignModelPatternInstanceRepository designModelPatternInstanceRepository;
//    private PatternViewDirectedEdgeRepository patternViewDirectedEdgeRepository;
//    private PatternViewUndirectedEdgeRepository patternViewUndirectedEdgeRepository;
//    private DirectedEdgeRepository directedEdgeRepository;
//    private UndirectedEdgeReository undirectedEdgeReository;

    public DesignModelServiceImpl(PatternService patternService,
                                  PatternRelationDescriptorService patternRelationDescriptorService,
                                  DesignModelRepository designModelRepository,
                                  DesignModelPatternInstanceRepository designModelPatternInstanceRepository,
                                  PatternViewDirectedEdgeRepository patternViewDirectedEdgeRepository,
                                  PatternViewUndirectedEdgeRepository patternViewUndirectedEdgeRepository,
                                  DirectedEdgeRepository directedEdgeRepository,
                                  UndirectedEdgeReository undirectedEdgeReository) {
        this.patternService = patternService;
//        this.patternRelationDescriptorService = patternRelationDescriptorService;
        this.designModelRepository = designModelRepository;
        this.designModelPatternInstanceRepository = designModelPatternInstanceRepository;
//        this.patternViewDirectedEdgeRepository = patternViewDirectedEdgeRepository;
//        this.patternViewUndirectedEdgeRepository = patternViewUndirectedEdgeRepository;
//        this.directedEdgeRepository = directedEdgeRepository;
//        this.undirectedEdgeReository = undirectedEdgeReository;
    }


    @Override
    @Transactional
    public DesignModel createDesignModel(DesignModel designModel) {
        if (null == designModel) {
            throw new NullDesignModelException();
        }

        return this.designModelRepository.save(designModel);
    }


    @Override
    @Transactional(readOnly = true)
    public List<DesignModel> getAllDesignModels() {
        return this.designModelRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public DesignModel getDesignModel(UUID designModelId) {
        DesignModel designModel = this.designModelRepository.findById(designModelId)
                .orElseThrow(() -> new DesignModelNotFoundException(designModelId));

          return designModel;
    }


//    @Override
//    @Transactional(readOnly = true)
//    public PatternView getPatternViewByUri(String uri) {
//        return this.patternViewRepository.findByUri(uri)
//                .orElseThrow(() -> new PatternViewNotFoundException(String.format("PatternView with URI \"%s\" not found!", uri)));
//    }
//
//    @Override
//    @Transactional
//    public PatternView updatePatternView(PatternView patternView) {
//        // We just support updating fields of PatternView but we don't support updates of sub resources such as pattern edges.
//        // So we ignore patterns and edges.
//        if (null == patternView) {
//            throw new NullPatternViewException();
//        }
//
//        if (!this.patternViewRepository.existsById(patternView.getId())) {
//            throw new PatternViewNotFoundException(patternView);
//        }
//
//        PatternView oldPatternView = this.getPatternViewById(patternView.getId());
//
//        patternView.setPatterns(oldPatternView.getPatterns());
//        patternView.setDirectedEdges(oldPatternView.getDirectedEdges());
//        patternView.setUndirectedEdges(oldPatternView.getUndirectedEdges());
//
//        return this.patternViewRepository.save(patternView);
//    }
//
//    @Override
//    @Transactional
//    public void deletePatternView(UUID patternViewId) {
//        PatternView patternView = this.getPatternViewById(patternViewId);
//
//        // Remove edges that are just part of pattern view
//        if (null != patternView.getDirectedEdges()) {
//            patternView.getDirectedEdges().forEach(patternViewDirectedEdge -> {
//                if (null == patternViewDirectedEdge.getDirectedEdge().getPatternLanguage()) {
//                    this.directedEdgeRepository.delete(patternViewDirectedEdge.getDirectedEdge());
//                }
//            });
//        }
//
//        if (null != patternView.getUndirectedEdges()) {
//            patternView.getUndirectedEdges().forEach(patternViewUndirectedEdge -> {
//                if (null == patternViewUndirectedEdge.getUndirectedEdge().getPatternLanguage()) {
//                    this.undirectedEdgeReository.delete(patternViewUndirectedEdge.getUndirectedEdge());
//                }
//            });
//        }
//
//        this.patternViewRepository.delete(patternView);
//    }
//
//    // Pattern Handling


    @Override
    @Transactional
    public void addPatternToDesignModel(UUID patternViewId, UUID patternId) {
        DesignModel designModel = this.getDesignModel(patternViewId);
        Pattern pattern = this.patternService.getPatternById(patternId);

        DesignModelPatternInstance designModelPattern = new DesignModelPatternInstance(designModel, pattern);
        this.designModelPatternInstanceRepository.save(designModelPattern);
    }


//    @Override
//    @Transactional(readOnly = true)
//    public List<Pattern> getPatternsOfPatternView(UUID patternViewId) {
//        return this.patternViewPatternRepository.findAllByPatternViewId(patternViewId).stream()
//                .map(PatternViewPattern::getPattern)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Pattern getPatternOfPatternViewById(UUID patternViewId, UUID patternId) {
//        PatternViewPattern patternViewPattern = this.patternViewPatternRepository.findById(new PatternViewPatternId(patternViewId, patternId))
//                .orElseThrow(() -> new PatternNotFoundException(patternViewId, patternId, PatternGraphType.PATTERN_VIEW));
//
//        return patternViewPattern.getPattern();
//    }
//
//    @Override
//    @Transactional
//    public void removePatternFromPatternView(UUID patternViewId, UUID patternId) {
//        PatternViewPatternId id = new PatternViewPatternId(patternViewId, patternId);
//        if (this.patternViewPatternRepository.existsById(id)) {
//            this.patternViewPatternRepository.deleteById(id);
//        } else {
//            throw new PatternNotFoundException(patternViewId, patternId, PatternGraphType.PATTERN_VIEW);
//        }
//    }
//
//    // DirectedEdge Handling
//
//    @Override
//    @Transactional
//    public void addDirectedEdgeToPatternView(UUID patternViewId, UUID directedEdgeId) {
//        PatternView patternView = this.getPatternViewById(patternViewId);
//        DirectedEdge directedEdge = this.patternRelationDescriptorService.getDirectedEdgeById(directedEdgeId);
//
//        PatternViewDirectedEdge patternViewDirectedEdge = new PatternViewDirectedEdge(patternView, directedEdge);
//        this.patternViewDirectedEdgeRepository.save(patternViewDirectedEdge);
//    }
//
//    @Override
//    @Transactional
//    public DirectedEdge createDirectedEdgeAndAddToPatternView(UUID patternViewId, AddDirectedEdgeToViewRequest request) {
//        PatternView patternView = this.getPatternViewById(patternViewId);
//
//        DirectedEdge directedEdge = new DirectedEdge();
//        directedEdge.setSource(this.patternService.getPatternById(request.getSourcePatternId()));
//        directedEdge.setTarget(this.patternService.getPatternById(request.getTargetPatternId()));
//        directedEdge.setDescription(request.getDescription());
//        directedEdge.setType(request.getType());
//        directedEdge = this.patternRelationDescriptorService.createDirectedEdge(directedEdge);
//
//        PatternViewDirectedEdge patternViewDirectedEdge = new PatternViewDirectedEdge(patternView, directedEdge);
//        patternViewDirectedEdge = this.patternViewDirectedEdgeRepository.save(patternViewDirectedEdge);
//
//        return patternViewDirectedEdge.getDirectedEdge();
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<DirectedEdge> getDirectedEdgesByPatternViewId(UUID patternViewId) {
//        return this.patternViewDirectedEdgeRepository.findByPatternViewId(patternViewId).stream()
//                .map(PatternViewDirectedEdge::getDirectedEdge)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public DirectedEdge getDirectedEdgeOfPatternViewById(UUID patternViewId, UUID directedEdgeId) {
//        return this.patternViewDirectedEdgeRepository.findById(new PatternViewDirectedEdgeId(patternViewId, directedEdgeId))
//                .map(PatternViewDirectedEdge::getDirectedEdge)
//                .orElseThrow(() -> new DirectedEdgeNotFoundException(patternViewId, directedEdgeId, PatternGraphType.PATTERN_VIEW));
//    }
//
//    @Override
//    @Transactional
//    public DirectedEdge updateDirectedEdgeOfPatternView(UUID patternViewId, UpdateDirectedEdgeRequest request) {
//
//        PatternViewDirectedEdge patternViewDirectedEdge = this.patternViewDirectedEdgeRepository
//                .findById(new PatternViewDirectedEdgeId(patternViewId, request.getDirectedEdgeId()))
//                .orElseThrow(() -> new DirectedEdgeNotFoundException(patternViewId, request.getDirectedEdgeId(), PatternGraphType.PATTERN_VIEW));
//
//        DirectedEdge directedEdge = patternViewDirectedEdge.getDirectedEdge();
//        directedEdge.setType(request.getType());
//        directedEdge.setDescription(request.getDescription());
//
//        Pattern source = this.patternService.getPatternById(request.getSourcePatternId());
//        directedEdge.setSource(source);
//
//        Pattern target = this.patternService.getPatternById(request.getTargetPatternId());
//        directedEdge.setTarget(target);
//
//        return this.patternRelationDescriptorService.updateDirectedEdge(directedEdge);
//    }
//
//    @Override
//    @Transactional
//    public void removeDirectedEdgeFromPatternView(UUID patternViewId, UUID directedEdgeId) throws DirectedEdgeNotFoundException {
//        DirectedEdge directedEdge = this.patternRelationDescriptorService.getDirectedEdgeById(directedEdgeId);
//        PatternView patternView = this.getPatternViewById(patternViewId);
//        PatternViewDirectedEdge patternViewDirectedEdge = this.patternViewDirectedEdgeRepository.getOne(new PatternViewDirectedEdgeId(patternViewId, directedEdgeId));
//
//        if (null != patternViewDirectedEdge) {
//            this.patternViewDirectedEdgeRepository.delete(patternViewDirectedEdge);
//        } else {
//            throw new DirectedEdgeNotFoundException(patternView, directedEdgeId);
//        }
//
//        if (null == directedEdge.getPatternLanguage()) {
//            // directed edge is not part of pattern language, thus remove it if it is not part of other views
//            if (!this.patternViewDirectedEdgeRepository.existsByDirectedEdgeId(directedEdgeId)) {
//                this.directedEdgeRepository.delete(directedEdge);
//            }
//        }
//    }
//
//    // UndirectedEdge Handling
//
//    @Override
//    @Transactional
//    public void addUndirectedEdgeToPatternView(UUID patternViewId, UUID undirectedEdgeId) {
//        PatternView patternView = this.getPatternViewById(patternViewId);
//        UndirectedEdge undirectedEdge = this.patternRelationDescriptorService.getUndirectedEdgeById(undirectedEdgeId);
//
//        PatternViewUndirectedEdge patternViewUndirectedEdge = new PatternViewUndirectedEdge(patternView, undirectedEdge);
//        this.patternViewUndirectedEdgeRepository.save(patternViewUndirectedEdge);
//    }
//
//    @Override
//    @Transactional
//    public UndirectedEdge createUndirectedEdgeAndAddToPatternView(UUID patternViewId, AddUndirectedEdgeToViewRequest request) {
//        PatternView patternView = this.getPatternViewById(patternViewId);
//
//        UndirectedEdge undirectedEdge = new UndirectedEdge();
//        undirectedEdge.setP1(this.patternService.getPatternById(request.getPattern1Id()));
//        undirectedEdge.setP2(this.patternService.getPatternById(request.getPattern2Id()));
//        undirectedEdge.setDescription(request.getDescription());
//        undirectedEdge.setType(request.getType());
//        undirectedEdge = this.patternRelationDescriptorService.createUndirectedEdge(undirectedEdge);
//
//        PatternViewUndirectedEdge patternViewUndirectedEdge = new PatternViewUndirectedEdge(patternView, undirectedEdge);
//        patternViewUndirectedEdge = this.patternViewUndirectedEdgeRepository.save(patternViewUndirectedEdge);
//
//        return patternViewUndirectedEdge.getUndirectedEdge();
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<UndirectedEdge> getUndirectedEdgesByPatternViewId(UUID patternViewId) {
//        return this.patternViewUndirectedEdgeRepository.findByPatternViewId(patternViewId).stream()
//                .map(PatternViewUndirectedEdge::getUndirectedEdge)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public UndirectedEdge getUndirectedEdgeOfPatternViewById(UUID patternViewId, UUID undirectedEdgeId) {
//        return this.patternViewUndirectedEdgeRepository.findById(new PatternViewUndirectedEdgeId(patternViewId, undirectedEdgeId))
//                .map(PatternViewUndirectedEdge::getUndirectedEdge)
//                .orElseThrow(() -> new UndirectedEdgeNotFoundException(patternViewId, undirectedEdgeId, PatternGraphType.PATTERN_VIEW));
//    }
//
//    @Override
//    @Transactional
//    public UndirectedEdge updateUndirectedEdgeOfPatternView(UUID patternViewId, UpdateUndirectedEdgeRequest request) {
//        PatternViewUndirectedEdge edge = this.patternViewUndirectedEdgeRepository
//                .findById(new PatternViewUndirectedEdgeId(patternViewId, request.getUndirectedEdgeId()))
//                .orElseThrow(() -> new UndirectedEdgeNotFoundException(patternViewId, request.getUndirectedEdgeId(), PatternGraphType.PATTERN_VIEW));
//
//        UndirectedEdge undirectedEdge = edge.getUndirectedEdge();
//        undirectedEdge.setType(request.getType());
//        undirectedEdge.setDescription(request.getDescription());
//
//        Pattern p1 = this.patternService.getPatternById(request.getPattern1Id());
//        undirectedEdge.setP1(p1);
//
//        Pattern p2 = this.patternService.getPatternById(request.getPattern2Id());
//        undirectedEdge.setP2(p2);
//
//        return this.patternRelationDescriptorService.updateUndirectedEdge(undirectedEdge);
//    }
//
//    @Override
//    @Transactional
//    public void removeUndirectedEdgeFromPatternView(UUID patternViewId, UUID undirectedEdgeId) {
//        UndirectedEdge undirectedEdge = this.patternRelationDescriptorService.getUndirectedEdgeById(undirectedEdgeId);
//        PatternView patternView = this.getPatternViewById(patternViewId);
//        PatternViewUndirectedEdge patternViewUndirectedEdge = this.patternViewUndirectedEdgeRepository
//                .getOne(new PatternViewUndirectedEdgeId(patternViewId, undirectedEdgeId));
//
//        if (null != patternViewUndirectedEdge) {
//            this.patternViewUndirectedEdgeRepository.delete(patternViewUndirectedEdge);
//        } else {
//            throw new UndirectedEdgeNotFoundException(patternView, undirectedEdgeId);
//        }
//
//        if (null == undirectedEdge.getPatternLanguage()) {
//            // directed edge is not part of pattern language, thus remove it if it is not part of other views
//            if (!this.patternViewUndirectedEdgeRepository.existsByUndirectedEdgeId(undirectedEdgeId)) {
//                this.undirectedEdgeReository.delete(undirectedEdge);
//            }
//        }
//    }
}
