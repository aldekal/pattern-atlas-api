package com.patternpedia.api.rest.model;

import java.util.UUID;

import com.patternpedia.api.entities.DirectedEdge;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class DirectedEdgeModel {
    private UUID id;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Object description;

    private String type;

    private String sourcePatternName;

    private UUID sourcePatternId;

    private String targetPatternName;

    private UUID targetPatternId;

    @JsonIgnore
    private DirectedEdge directedEdge;

    private DirectedEdgeModel(DirectedEdge directedEdge) {
        this.id = directedEdge.getId();
        this.description = directedEdge.getDescription();
        this.type = directedEdge.getType();
        this.sourcePatternId = directedEdge.getSource().getId();
        this.sourcePatternName = directedEdge.getSource().getName();
        this.targetPatternId = directedEdge.getTarget().getId();
        this.targetPatternName = directedEdge.getTarget().getName();
        this.directedEdge = directedEdge;
    }

    public static DirectedEdgeModel from(DirectedEdge directedEdge) {
        return new DirectedEdgeModel(directedEdge);
    }
}
