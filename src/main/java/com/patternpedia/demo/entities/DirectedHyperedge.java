package com.patternpedia.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectedHyperedge extends PatternRelationDescriptor {

    @OneToMany
    private Set<Pattern> sourceSet;

    @OneToMany
    private Set<Pattern> targetSet;

    private String description;
}
