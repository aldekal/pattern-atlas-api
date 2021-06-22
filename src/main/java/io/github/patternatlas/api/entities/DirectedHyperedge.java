package io.github.patternatlas.api.entities;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class DirectedHyperedge extends PatternRelationDescriptor {

    @OneToMany
    private Set<Pattern> sourceSet;

    @OneToMany
    private Set<Pattern> targetSet;

    private String type;

    private String label;
}
