package com.patternpedia.api.rest.representationModel;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;

import java.net.URL;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@JsonTypeName("patternLanguage")
public class PatternLanguageModel extends RepresentationModel<PatternLanguageModel> {
    private UUID id;
    private String uri;
    private String name;
    private URL logo;
    private CollectionModel<PatternModel> patterns;
    // private CollectionModel<EntityModel<DirectedEdge>> directedEdges;
    // private CollectionModel<EntityModel<UndirectedEdge>> undirectedEdges;
}
