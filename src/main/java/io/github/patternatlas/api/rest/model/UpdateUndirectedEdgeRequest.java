package io.github.patternatlas.api.rest.model;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UpdateUndirectedEdgeRequest {
    private UUID undirectedEdgeId;
    private String type;
    private Object description;
    private UUID pattern1Id;
    private UUID pattern2Id;
}
