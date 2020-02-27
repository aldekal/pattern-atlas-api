package com.patternpedia.api.entities.rating;

import com.patternpedia.api.entities.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class RatingUp {

    @EmbeddedId
    @EqualsAndHashCode.Exclude
    private RatingKey id;

    @ManyToOne
    @MapsId("patternId")
    @EqualsAndHashCode.Include
    private PatternEvolution patternEvolutionUp;

    @ManyToOne
    @MapsId("userId")
    @EqualsAndHashCode.Include
    private UserEntity userUp;

    private int rating;

    public RatingUp(PatternEvolution patternEvolution, UserEntity user) {
        this.patternEvolutionUp = patternEvolution;
        this.userUp = user;
        this.id = new RatingKey(patternEvolution.getId(), user.getId());
    }
}
