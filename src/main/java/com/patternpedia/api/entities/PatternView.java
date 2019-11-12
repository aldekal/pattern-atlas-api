package com.patternpedia.api.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PatternView extends PatternGraph {

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "pattern_view_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "pattern_id", referencedColumnName = "id")
    )
    private List<Pattern> patterns;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "pattern_view_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "directed_edge_id", referencedColumnName = "id")
    )
    private List<DirectedEdge> directedEdges;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "pattern_view_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "undirected_edge_id", referencedColumnName = "id")
    )
    private List<UndirectedEdge> undirectedEdges;
}
