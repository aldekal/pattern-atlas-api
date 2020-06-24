package com.patternpedia.api.service;

import com.patternpedia.api.entities.designmodel.ConcreteSolution;
import com.patternpedia.api.exception.ConcreteSolutionNotFoundException;
import com.patternpedia.api.repositories.ConcreteSolutionRepository;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.UUID;


@Service
public class ConcreteSolutionServiceImpl implements ConcreteSolutionService {

    private final ConcreteSolutionRepository concreteSolutionRepository;


    public ConcreteSolutionServiceImpl(ConcreteSolutionRepository concreteSolutionRepository) {
        this.concreteSolutionRepository = concreteSolutionRepository;
    }


    public List<ConcreteSolution> getConcreteSolutions() {
        return this.concreteSolutionRepository.findAll();
    }


    public List<ConcreteSolution> getConcreteSolutions(URI patternUri) {
        return this.concreteSolutionRepository.findAllByPatternUri(patternUri.toString());
    }


    public ConcreteSolution getConcreteSolution(UUID uuid) {
        return this.concreteSolutionRepository.findTopById(uuid)
                .orElseThrow(() -> new ConcreteSolutionNotFoundException(uuid));
    }
}
