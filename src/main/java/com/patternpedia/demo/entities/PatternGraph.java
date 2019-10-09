package com.patternpedia.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SortNatural;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.SortedSet;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class PatternGraph extends EntityWithURI {

    @OneToMany
    @SortNatural
    private SortedSet<Pattern> patterns;

    @OneToMany
    private Set<PatternRelationDescriptor> patternRelationDescriptors;
}
