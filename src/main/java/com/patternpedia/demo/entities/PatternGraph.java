package com.patternpedia.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.SortedSet;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class PatternGraph extends EntityWithURI {

    @OneToMany(fetch = FetchType.EAGER)
    @SortNatural
    private SortedSet<Pattern> patterns;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<PatternRelationDescriptor> patternRelationDescriptors;

    public PatternGraph(String uri, String name, SortedSet<Pattern> patterns, Set<PatternRelationDescriptor> patternRelationDescriptors) throws UnsupportedEncodingException {
        super(uri, name);
        this.patterns = patterns;
        this.patternRelationDescriptors = patternRelationDescriptors;
    }
}
