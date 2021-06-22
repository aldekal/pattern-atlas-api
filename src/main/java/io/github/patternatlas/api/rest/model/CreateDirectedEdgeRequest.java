package io.github.patternatlas.api.rest.model;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CreateDirectedEdgeRequest {
    private UUID sourcePatternId;
    private UUID targetPatternId;
    private String type;
    private Object description;

    private CreateDirectedEdgeRequest(UUID sourcePatternId, UUID targetPatternId) {
        this.sourcePatternId = sourcePatternId;
        this.targetPatternId = targetPatternId;
    }

    public static CreateDirectedEdgeRequestBuilder builder(UUID sourcePatternId, UUID targetPatternId) {
        return new CreateDirectedEdgeRequestBuilder(sourcePatternId, targetPatternId);
    }

    public static class CreateDirectedEdgeRequestBuilder {
        private final CreateDirectedEdgeRequest createDirectedEdgeRequest;

        public CreateDirectedEdgeRequestBuilder(UUID sourcePatternId, UUID targetPatternId) {
            this.createDirectedEdgeRequest = new CreateDirectedEdgeRequest(sourcePatternId, targetPatternId);
        }

        public CreateDirectedEdgeRequestBuilder withType(String type) {
            this.createDirectedEdgeRequest.setType(type);
            return this;
        }

        public CreateDirectedEdgeRequestBuilder withDescription(Object description) {
            this.createDirectedEdgeRequest.setDescription(description);
            return this;
        }

        public CreateDirectedEdgeRequest build() {
            return this.createDirectedEdgeRequest;
        }
    }
}
