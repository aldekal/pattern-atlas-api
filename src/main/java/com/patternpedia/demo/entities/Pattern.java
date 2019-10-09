package com.patternpedia.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Data
public class Pattern extends EntityWithURI implements Comparable<Pattern> {

    @Override
    public int compareTo(Pattern o) {
        return this.getName().compareTo(o.getName());
    }

    /*@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private JsonNode content;*/
}
