package com.patternpedia.api.entities.edge;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.patternpedia.api.entities.pattern.pattern.Pattern;
import com.patternpedia.api.entities.PatternRelationDescriptor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
