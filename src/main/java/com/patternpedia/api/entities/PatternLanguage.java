package com.patternpedia.api.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.net.URL;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PatternLanguage extends PatternGraph {

    private URL logo;

    @OneToMany(mappedBy = "patternLanguage")
    private List<Pattern> patterns;

    @OneToOne(mappedBy = "patternLanguage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PatternSchema patternSchema;

    @OneToMany
    private List<DirectedEdge> directedEdges;

    @OneToMany
    private List<UndirectedEdge> undirectedEdges;

}
