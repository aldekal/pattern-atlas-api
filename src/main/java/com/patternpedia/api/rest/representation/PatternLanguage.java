package com.patternpedia.api.rest.representation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.net.URL;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PatternLanguage extends RepresentationModel<PatternLanguage> {
    private UUID id;
    private String uri;
    private String name;
    private URL logo;
    private List<Pattern> patterns;
}
