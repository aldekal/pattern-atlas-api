package com.patternpedia.api.service;

import com.patternpedia.api.entities.designmodel.ConcreteSolution;

import java.net.URI;
import java.util.List;
import java.util.UUID;


public interface ConcreteSolutionService {

    List<ConcreteSolution> getConcreteSolutions();

    List<ConcreteSolution> getConcreteSolutions(URI patternUri);

    ConcreteSolution getConcreteSolution(UUID uuid);
}
