package com.patternpedia.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DirectedEdge extends PatternRelationDescriptor{

    @ManyToOne
    private Pattern source;

    @ManyToOne
    private Pattern target;

    private String description;
}
