package com.patternpedia.api.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "p1PatternIdx", columnList = "p1_id"),
        @Index(name = "p2PatternIdx", columnList = "p2_id")
})
public class UndirectedEdge extends PatternRelationDescriptor {

    @JsonIgnore
    @ManyToOne(optional = false)
    private Pattern p1;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Pattern p2;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private PatternLanguage patternLanguage;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "undirectedEdges")
    private List<PatternView> patternViews;
}
