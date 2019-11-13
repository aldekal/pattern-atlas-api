package com.patternpedia.api.rest.representation;

import com.fasterxml.jackson.annotation.JsonRootName;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;

@JsonRootName("patternLanguages")
public class PatternLanguageCollectionModel<T> extends CollectionModel<T> {
    public PatternLanguageCollectionModel(Iterable<T> content, Link... links) {
        super(content, links);
    }
}
