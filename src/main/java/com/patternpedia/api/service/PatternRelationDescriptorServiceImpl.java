package com.patternpedia.api.service;

import com.fasterxml.jackson.databind.JsonNode;
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

import java.util.Collections;
import java.util.UUID;

@Service
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
    public DirectedEdge createDirectedEdge(Pattern source, Pattern target, UUID patternLanguageId, JsonNode description) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);

        DirectedEdge directedEdge = new DirectedEdge();
        directedEdge.setSource(source);
        directedEdge.setTarget(target);
        directedEdge.setPatternLanguage(patternLanguage);
        directedEdge.setDescription(description);

        directedEdge = this.directedEdgeRepository.save(directedEdge);
        if (null != patternLanguage.getDirectedEdges()) {
            patternLanguage.getDirectedEdges().add(directedEdge);
        } else {
            patternLanguage.setDirectedEdges(Collections.singletonList(directedEdge));
        }
        this.patternLanguageService.updatePatternLanguage(patternLanguage);
        return directedEdge;
    }

    @Override
    public DirectedEdge getDirectedEdgeById(UUID id) {
        return this.directedEdgeRepository.findById(id)
                .orElseThrow(() -> new DirectedEdgeNotFoundException(String.format("DirectedEdge %s not found!", id.toString())));
    }

    @Override
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
    public UndirectedEdge createUndirectedEdge(Pattern p1, Pattern p2, UUID patternLanguageId, JsonNode description) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);
        UndirectedEdge undirectedEdge = new UndirectedEdge();
        undirectedEdge.setP1(p1);
        undirectedEdge.setP2(p2);
        undirectedEdge.setPatternLanguage(patternLanguage);
        undirectedEdge.setDescription(description);

        undirectedEdge = this.undirectedEdgeReository.save(undirectedEdge);
        if (null != patternLanguage.getDirectedEdges()) {
            patternLanguage.getUndirectedEdges().add(undirectedEdge);
        } else {
            patternLanguage.setUndirectedEdges(Collections.singletonList(undirectedEdge));
        }
        this.patternLanguageService.updatePatternLanguage(patternLanguage);
        return undirectedEdge;
    }

    @Override
    public UndirectedEdge getUndirectedEdgeById(UUID id) {
        return this.undirectedEdgeReository.findById(id)
                .orElseThrow(() -> new UndirectedEdgeNotFoundException(String.format("UndirectedEdge %s not found!", id.toString())));
    }

    @Override
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
