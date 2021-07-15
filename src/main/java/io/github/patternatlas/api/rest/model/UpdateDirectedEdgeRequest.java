package io.github.patternatlas.api.rest.model;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UpdateDirectedEdgeRequest {
    private UUID directedEdgeId;
    private String type;
    private Object description;
    private UUID sourcePatternId;
    private UUID targetPatternId;
}
