package io.github.patternatlas.api.rest.model;

import java.util.UUID;

import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.github.patternatlas.api.entities.designmodel.DesignModelPatternEdge;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode
@Relation(value = "edge", collectionRelation = "edges")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EdgeDTO {

    private UUID sourcePatternId;

    private UUID targetPatternId;

    private UUID pattern1Id;

    private UUID pattern2Id;

    private String type;

    private String description;

    public boolean isDirectedEdge() {
        return sourcePatternId != null && targetPatternId != null;
    }

    @JsonIgnore
    public UUID getFirstPatternId() {
        return isDirectedEdge() ? sourcePatternId : pattern1Id;
    }

    @JsonIgnore
    public UUID getSecondPatternId() {
        return isDirectedEdge() ? targetPatternId : pattern2Id;
    }

    public static EdgeDTO from(DesignModelPatternEdge designModelPatternEdge) {
        EdgeDTO edgeDTO = new EdgeDTO();

        UUID pattern1 = designModelPatternEdge.getPatternInstance1().getPatternInstanceId();
        UUID pattern2 = designModelPatternEdge.getPatternInstance2().getPatternInstanceId();

        if (designModelPatternEdge.isDirectedEdge()) {
            edgeDTO.setSourcePatternId(pattern1);
            edgeDTO.setTargetPatternId(pattern2);
        } else {
            edgeDTO.setPattern1Id(pattern1);
            edgeDTO.setPattern2Id(pattern2);
        }

        edgeDTO.setType(designModelPatternEdge.getType());
        edgeDTO.setDescription(designModelPatternEdge.getDescription());

        return edgeDTO;
    }
}
