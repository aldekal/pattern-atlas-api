package io.github.ust.quantil.patternatlas.api.service;

import io.github.ust.quantil.patternatlas.api.entities.designmodel.ConcreteSolution;
import io.github.ust.quantil.patternatlas.api.entities.designmodel.DesignModelPatternEdge;
import io.github.ust.quantil.patternatlas.api.entities.designmodel.DesignModelPatternInstance;
import io.github.ust.quantil.patternatlas.api.rest.model.FileDTO;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public interface ConcreteSolutionService {

    List<ConcreteSolution> getConcreteSolutions();

    List<ConcreteSolution> getConcreteSolutions(URI patternUri);

    ConcreteSolution getConcreteSolution(UUID uuid);

    List<FileDTO> aggregate(List<DesignModelPatternInstance> patternInstances, List<DesignModelPatternEdge> edges, Map<UUID, UUID> concreteSolutionMapping);
}
