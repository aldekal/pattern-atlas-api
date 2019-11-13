package com.patternpedia.api.rest.representation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Pattern extends RepresentationModel<Pattern> {
    private UUID id;
    private String uri;
    private String name;
}
