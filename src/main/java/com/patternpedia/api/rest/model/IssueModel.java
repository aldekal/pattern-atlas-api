package com.patternpedia.api.rest.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

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
