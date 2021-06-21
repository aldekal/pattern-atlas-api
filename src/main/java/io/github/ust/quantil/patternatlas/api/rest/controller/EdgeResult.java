package io.github.ust.quantil.patternatlas.api.rest.controller;

import java.util.List;

import org.springframework.hateoas.Link;

import io.github.ust.quantil.patternatlas.api.entities.DirectedEdge;
import io.github.ust.quantil.patternatlas.api.entities.UndirectedEdge;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EdgeResult {

    private List<DirectedEdge> outgoingEdges;
    private List<DirectedEdge> ingoingEdges;
    private List<UndirectedEdge> undirectedEdges;

    private List<Link> standardPatternLinks;
}
