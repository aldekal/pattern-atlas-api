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
public class PatternViewUndirectedEdge {

    @EmbeddedId
    @EqualsAndHashCode.Exclude
    private PatternViewUndirectedEdgeId id;

    @ManyToOne
    @MapsId("patternViewId")
    @EqualsAndHashCode.Include
    private PatternView patternView;

    @ManyToOne
    @MapsId("undirectedEdgeId")
    @EqualsAndHashCode.Include
    private UndirectedEdge undirectedEdge;

    public PatternViewUndirectedEdge(PatternView patternView, UndirectedEdge undirectedEdge) {
        this.patternView = patternView;
        this.undirectedEdge = undirectedEdge;
        this.id = new PatternViewUndirectedEdgeId(patternView.getId(), undirectedEdge.getId());
    }
}
