package io.github.patternatlas.api.rest.controller;

import java.util.List;

import org.springframework.hateoas.Link;

import io.github.patternatlas.api.entities.DirectedEdge;
import io.github.patternatlas.api.entities.UndirectedEdge;
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
