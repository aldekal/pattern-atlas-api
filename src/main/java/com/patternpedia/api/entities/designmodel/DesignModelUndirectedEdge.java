package com.patternpedia.api.entities.designmodel;

import com.patternpedia.api.entities.UndirectedEdge;
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
public class DesignModelUndirectedEdge {

    @EmbeddedId
    @EqualsAndHashCode.Exclude
    private DesignModelUndirectedEdgeId id;

    @ManyToOne
    @MapsId("designModelId")
    @EqualsAndHashCode.Include
    private DesignModel designModel;

    @ManyToOne
    @MapsId("undirectedEdgeId")
    @EqualsAndHashCode.Include
    private UndirectedEdge undirectedEdge;

    public DesignModelUndirectedEdge(DesignModel designModel, UndirectedEdge undirectedEdge) {
        this.designModel = designModel;
        this.undirectedEdge = undirectedEdge;
        this.id = new DesignModelUndirectedEdgeId(designModel.getId(), undirectedEdge.getId());
    }
}
