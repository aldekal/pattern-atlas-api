package io.github.ust.quantil.patternatlas.api.rest.controller;

import io.github.ust.quantil.patternatlas.api.entities.DirectedEdge;
import io.github.ust.quantil.patternatlas.api.entities.UndirectedEdge;
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
