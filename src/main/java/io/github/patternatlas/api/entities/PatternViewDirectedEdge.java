package io.github.patternatlas.api.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class PatternViewDirectedEdge {

    @EmbeddedId
    @EqualsAndHashCode.Exclude
    private PatternViewDirectedEdgeId id;

    @ManyToOne
    @MapsId("patternViewId")
    @EqualsAndHashCode.Include
    private PatternView patternView;

    @ManyToOne
    @MapsId("directedEdgeId")
    @EqualsAndHashCode.Include
    private DirectedEdge directedEdge;

    public PatternViewDirectedEdge(PatternView patternView, DirectedEdge directedEdge) {
        this.patternView = patternView;
        this.directedEdge = directedEdge;
        this.id = new PatternViewDirectedEdgeId(patternView.getId(), directedEdge.getId());
    }
}
