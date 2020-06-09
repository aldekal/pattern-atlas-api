package com.patternpedia.api.entities.designmodel;

import com.patternpedia.api.entities.DirectedEdge;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class DesignModelDirectedEdge {

    @EmbeddedId
    @EqualsAndHashCode.Exclude
    private DesignModelDirectedEdgeId id;

    @ManyToOne
    @MapsId("designModelId")
    @EqualsAndHashCode.Include
    private DesignModel designModel;

    @ManyToOne
    @MapsId("directedEdgeId")
    @EqualsAndHashCode.Include
    private DirectedEdge directedEdge;

    public DesignModelDirectedEdge(DesignModel designModel, DirectedEdge directedEdge) {
        this.designModel = designModel;
        this.directedEdge = directedEdge;
        this.id = new DesignModelDirectedEdgeId(designModel.getId(), directedEdge.getId());
    }
}
