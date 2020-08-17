package com.patternpedia.api.service;

import com.patternpedia.api.entities.designmodel.ConcreteSolution;
import com.patternpedia.api.entities.designmodel.DesignModelPatternEdge;
import com.patternpedia.api.entities.designmodel.DesignModelPatternInstance;
import com.patternpedia.api.rest.model.FileDTO;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public interface ConcreteSolutionService {

    List<ConcreteSolution> getConcreteSolutions();

    List<ConcreteSolution> getConcreteSolutions(URI patternUri);

    ConcreteSolution getConcreteSolution(UUID uuid);

//    ConcreteSolution getConcreteSolution(URI patternUri, String technology);

    List<FileDTO> aggregate(List<DesignModelPatternInstance> patternInstances, List<DesignModelPatternEdge> edges, Map<UUID, UUID> concreteSolutionMapping);
}
