package com.patternpedia.api.entities;

import java.net.URL;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
