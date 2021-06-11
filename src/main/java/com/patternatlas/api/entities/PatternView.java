package com.patternatlas.api.entities;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PatternView extends PatternGraph {

    private URL logo;

    @JsonIgnore
    @OneToMany(mappedBy = "patternView", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatternViewPattern> patterns = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "patternView", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatternViewDirectedEdge> directedEdges;

    @JsonIgnore
    @OneToMany(mappedBy = "patternView", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatternViewUndirectedEdge> undirectedEdges;

    public void removePattern(Pattern pattern) {
        for (Iterator<PatternViewPattern> iterator = this.patterns.iterator(); iterator.hasNext(); ) {
            PatternViewPattern patternViewPattern = iterator.next();
            if (patternViewPattern.getPatternView().equals(this) && patternViewPattern.getPattern().equals(pattern)) {
                iterator.remove();
                patternViewPattern.getPattern().getPatternViews().remove(patternViewPattern);
                patternViewPattern.setPattern(null);
                patternViewPattern.setPatternView(null);
                break;
            }
        }
    }

    public void removeDirectedEdge(DirectedEdge directedEdge) {
        for (Iterator<PatternViewDirectedEdge> iterator = this.directedEdges.iterator(); iterator.hasNext(); ) {
            PatternViewDirectedEdge patternViewDirectedEdge = iterator.next();
            if (patternViewDirectedEdge.getPatternView().equals(this) &&
                    patternViewDirectedEdge.getDirectedEdge().equals(directedEdge)) {
                iterator.remove();
                patternViewDirectedEdge.getDirectedEdge().getPatternViews().remove(patternViewDirectedEdge);
                patternViewDirectedEdge.setDirectedEdge(null);
                patternViewDirectedEdge.setPatternView(null);
                break;
            }
        }
    }
}
