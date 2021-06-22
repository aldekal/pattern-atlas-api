package io.github.patternatlas.api.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class PatternViewPattern {

    @EmbeddedId
    @EqualsAndHashCode.Exclude
    private PatternViewPatternId id;

    @ManyToOne
    @MapsId("patternViewId")
    @EqualsAndHashCode.Include
    private PatternView patternView;

    @ManyToOne
    @MapsId("patternId")
    @EqualsAndHashCode.Include
    private Pattern pattern;

    public PatternViewPattern(PatternView patternView, Pattern pattern) {
        this.patternView = patternView;
        this.pattern = pattern;
        this.id = new PatternViewPatternId(patternView.getId(), pattern.getId());
    }
}
