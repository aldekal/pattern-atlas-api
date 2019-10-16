package com.patternpedia.api.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.net.URL;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PatternLanguage extends PatternGraph {

    private URL logo;

    @OneToMany
    private List<Pattern> patterns;

    @OneToOne
    private PatternSchema patternSchema;

    @OneToMany
    private List<DirectedEdge> directedEdges;

    @OneToMany
    private List<UndirectedEdge> undirectedEdges;

}
