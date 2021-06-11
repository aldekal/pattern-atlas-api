package com.patternatlas.api.service;

import com.patternatlas.api.entities.designmodel.ConcreteSolution;
import com.patternatlas.api.entities.designmodel.DesignModelPatternEdge;
import com.patternatlas.api.entities.designmodel.DesignModelPatternInstance;
import com.patternatlas.api.rest.model.FileDTO;

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
