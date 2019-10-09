package com.patternpedia.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UndirectedEdge extends PatternRelationDescriptor {

    @ManyToOne
    private Pattern p1;

    @ManyToOne
    private Pattern p2;

    private String Description;
}
