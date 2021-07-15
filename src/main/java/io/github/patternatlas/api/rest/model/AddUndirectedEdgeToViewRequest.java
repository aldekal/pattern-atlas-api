package io.github.patternatlas.api.rest.model;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AddUndirectedEdgeToViewRequest {
    private UUID undirectedEdgeId;

    private UUID pattern1Id;

    private UUID pattern2Id;

    private boolean isNewEdge;

    private Object description;

    private String type;
}
