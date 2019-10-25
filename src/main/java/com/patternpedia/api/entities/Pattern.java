package com.patternpedia.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.patternpedia.api.validator.PatternContentConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Pattern extends EntityWithURI {

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private PatternLanguage patternLanguage;

    @JsonIgnore
    @ManyToMany(mappedBy = "patterns")
    private List<PatternView> patternViews;

    @ManyToOne
    private PatternSchema patternSchema;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Object content;

}
