package com.patternpedia.api.rest.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import java.util.UUID;


@NoArgsConstructor
@Data
@EqualsAndHashCode
@Relation(value = "edge", collectionRelation = "edges")
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

    public UUID getFirstPatternId() {
        return isDirectedEdge() ? sourcePatternId : pattern1Id;
    }

    public UUID getSecondPatternId() {
        return isDirectedEdge() ? targetPatternId : pattern2Id;
    }
}
