package io.github.patternatlas.api.entities;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @OneToMany(mappedBy = "undirectedEdge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatternViewUndirectedEdge> patternViews;
}
