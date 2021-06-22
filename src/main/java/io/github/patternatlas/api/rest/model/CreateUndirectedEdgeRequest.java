package io.github.patternatlas.api.rest.model;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CreateUndirectedEdgeRequest {
    private UUID p1Id;
    private UUID p2Id;
    private String type;
    private Object description;

    private CreateUndirectedEdgeRequest(UUID p1Id, UUID p2Id) {
        this.p1Id = p1Id;
        this.p2Id = p2Id;
    }

    public static CreateUndirectedEdgeRequestBuilder builder(UUID p1Id, UUID p2Id) {
        return new CreateUndirectedEdgeRequestBuilder(p1Id, p2Id);
    }

    public static class CreateUndirectedEdgeRequestBuilder {
        private final CreateUndirectedEdgeRequest createUndirectedEdgeRequest;

        public CreateUndirectedEdgeRequestBuilder(UUID sourcePatternid, UUID targetPatternId) {
            this.createUndirectedEdgeRequest = new CreateUndirectedEdgeRequest(sourcePatternid, targetPatternId);
        }

        public CreateUndirectedEdgeRequestBuilder withType(String type) {
            this.createUndirectedEdgeRequest.setType(type);
            return this;
        }

        public CreateUndirectedEdgeRequestBuilder withDescription(Object description) {
            this.createUndirectedEdgeRequest.setDescription(description);
            return this;
        }

        public CreateUndirectedEdgeRequest build() {
            return this.createUndirectedEdgeRequest;
        }
    }
}
