package com.patternpedia.api.rest.representation;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;

import java.net.URL;
import java.util.UUID;

@Data
@NoArgsConstructor
public class PatternLanguageModel extends RepresentationModel<PatternLanguageModel> {
    private UUID id;
    private String uri;
    private String name;
    private URL logo;
    private CollectionModel<PatternModel> patternModels;
}
