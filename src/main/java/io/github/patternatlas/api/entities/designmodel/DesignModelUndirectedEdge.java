package io.github.patternatlas.api.entities.designmodel;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import io.github.patternatlas.api.entities.UndirectedEdge;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
