package com.patternpedia.api.rest.model;

import java.util.UUID;

import com.patternpedia.api.entities.UndirectedEdge;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

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

    private String pattern2Name;

    private UUID pattern2Id;

    @JsonIgnore
    private UndirectedEdge undirectedEdge;

    private UndirectedEdgeModel(UndirectedEdge undirectedEdge) {
        this.id = undirectedEdge.getId();
        this.description = undirectedEdge.getDescription();
        this.type = undirectedEdge.getType();
        this.pattern1Name = undirectedEdge.getP1().getName();
        this.pattern1Id = undirectedEdge.getP1().getId();
        this.pattern2Name = undirectedEdge.getP2().getName();
        this.pattern2Id = undirectedEdge.getP2().getId();
        this.undirectedEdge = undirectedEdge;
    }

    public static UndirectedEdgeModel from(UndirectedEdge undirectedEdge) {
        return new UndirectedEdgeModel(undirectedEdge);
    }
}
