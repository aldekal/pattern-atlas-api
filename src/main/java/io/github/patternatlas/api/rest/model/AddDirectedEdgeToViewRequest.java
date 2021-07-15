package io.github.patternatlas.api.rest.model;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AddDirectedEdgeToViewRequest {
    private UUID directedEdgeId;

    private UUID sourcePatternId;

    private UUID targetPatternId;

    private boolean isNewEdge;

    private Object description;

    private String type;
}
