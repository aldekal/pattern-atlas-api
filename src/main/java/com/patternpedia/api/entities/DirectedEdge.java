package com.patternpedia.api.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DirectedEdge extends PatternRelationDescriptor {

    @ManyToOne(optional = false)
    private Pattern source;

    @ManyToOne(optional = false)
    private Pattern target;

}
