package io.github.patternatlas.api.rest.model;

import java.util.UUID;
import javax.persistence.Column;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.github.patternatlas.api.entities.UndirectedEdge;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class UndirectedEdgeModel {
    private UUID id;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Object description;

    private String type;

    private String pattern1Name;

    private UUID pattern1Id;

    private String pattern1Uri;

    private String pattern2Name;

    private UUID pattern2Id;

    private String pattern2Uri;

    @JsonIgnore
    private UndirectedEdge undirectedEdge;

    private UndirectedEdgeModel(UndirectedEdge undirectedEdge) {
        this.id = undirectedEdge.getId();
        this.description = undirectedEdge.getDescription();
        this.type = undirectedEdge.getType();
        this.pattern1Name = undirectedEdge.getP1().getName();
        this.pattern1Id = undirectedEdge.getP1().getId();
        this.pattern1Uri = undirectedEdge.getP1().getUri();
        this.pattern2Name = undirectedEdge.getP2().getName();
        this.pattern2Id = undirectedEdge.getP2().getId();
        this.pattern2Uri = undirectedEdge.getP2().getUri();
        this.undirectedEdge = undirectedEdge;
    }

    public static UndirectedEdgeModel from(UndirectedEdge undirectedEdge) {
        return new UndirectedEdgeModel(undirectedEdge);
    }
}
