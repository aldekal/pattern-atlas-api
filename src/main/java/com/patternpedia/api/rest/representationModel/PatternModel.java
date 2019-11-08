package com.patternpedia.api.rest.representationModel;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@JsonRootName("pattern")
public class PatternModel extends RepresentationModel<PatternModel> {
    private UUID id;
    private String uri;
    private String name;
}
