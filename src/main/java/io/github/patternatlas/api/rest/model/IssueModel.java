package io.github.patternatlas.api.rest.model;

import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class IssueModel {

    private UUID id;

    private UUID pattern_language_id;

    private UUID[] author_group;

    private UUID[] user_voted;

    private Integer votes;

    private String name;

    private String description;

    private String version;

    private Boolean active;
}
