package com.patternpedia.api.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class UndirectedHyperedge extends PatternRelationDescriptor {

    @OneToMany
    private Set<Pattern> patterns;

    private String type;

    private String label;

}
