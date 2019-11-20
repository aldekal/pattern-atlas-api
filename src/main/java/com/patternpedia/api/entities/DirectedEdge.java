package com.patternpedia.api.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DirectedEdge extends PatternRelationDescriptor {

    @ManyToOne(optional = false)
    private Pattern source;

    @ManyToOne(optional = false)
    private Pattern target;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private PatternLanguage patternLanguage;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "directedEdges")
    private List<PatternView> patternViews;
}
