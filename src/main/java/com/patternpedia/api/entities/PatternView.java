package com.patternpedia.api.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PatternView extends PatternGraph {

    @JsonIgnore
    @OneToMany(mappedBy = "patternView", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatternViewPattern> patterns = new ArrayList<>();

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
}
