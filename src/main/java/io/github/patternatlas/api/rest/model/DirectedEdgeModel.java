package io.github.patternatlas.api.rest.model;

import java.util.UUID;
import javax.persistence.Column;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.github.patternatlas.api.entities.DirectedEdge;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    private String sourcePatternUri;

    private String targetPatternName;

    private UUID targetPatternId;

    private String targetPatternUri;

    @JsonIgnore
    private DirectedEdge directedEdge;

    private DirectedEdgeModel(DirectedEdge directedEdge) {
        this.id = directedEdge.getId();
        this.description = directedEdge.getDescription();
        this.type = directedEdge.getType();
        this.sourcePatternId = directedEdge.getSource().getId();
        this.sourcePatternName = directedEdge.getSource().getName();
        this.sourcePatternUri = directedEdge.getSource().getUri();
        this.targetPatternId = directedEdge.getTarget().getId();
        this.targetPatternName = directedEdge.getTarget().getName();
        this.targetPatternUri = directedEdge.getTarget().getUri();
        this.directedEdge = directedEdge;
    }

    public static DirectedEdgeModel from(DirectedEdge directedEdge) {
        return new DirectedEdgeModel(directedEdge);
    }
}
