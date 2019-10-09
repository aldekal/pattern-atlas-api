package com.patternpedia.api.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class UndirectedEdge extends PatternRelationDescriptor {

    @ManyToOne(optional = false)
    private Pattern p1;

    @ManyToOne(optional = false)
    private Pattern p2;

}
