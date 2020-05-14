package com.patternpedia.api.rest.controller;

import com.patternpedia.api.entities.DirectedEdge;
import com.patternpedia.api.entities.UndirectedEdge;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.Link;

import java.util.List;

@Data
@AllArgsConstructor
public class EdgeResult {

    private List<DirectedEdge> outgoingEdges;
    private List<DirectedEdge> ingoingEdges;
    private List<UndirectedEdge> undirectedEdges;

    private List<Link> standardPatternLinks;
}
