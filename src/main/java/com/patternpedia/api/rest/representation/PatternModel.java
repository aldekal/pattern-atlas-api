package com.patternpedia.api.rest.representation;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Data
@NoArgsConstructor
public class PatternModel extends RepresentationModel<PatternModel> {
    private UUID id;
    private String uri;
    private String name;
}
