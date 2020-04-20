package com.patternpedia.api.entities.rating;

import com.patternpedia.api.entities.evolution.PatternEvolution;
import com.patternpedia.api.entities.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
//@EqualsAndHashCode
public class RatingPatternEvolution {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private RatingKey id;

    @ManyToOne
    @MapsId("patternId")
    @EqualsAndHashCode.Include
    private PatternEvolution patternEvolution;

    @ManyToOne
    @MapsId("userId")
    @EqualsAndHashCode.Include
    private UserEntity user;

    private int rating;

    public RatingPatternEvolution(PatternEvolution patternEvolution, UserEntity user) {
        this.patternEvolution = patternEvolution;
        this.user = user;
        this.id = new RatingKey(patternEvolution.getId(), user.getId());
    }

    @Override
    public String toString() {
        return this.id.toString() + this.rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RatingPatternEvolution)) return false;
        RatingPatternEvolution that = (RatingPatternEvolution) o;
        return Objects.equals(patternEvolution.getName(), that.patternEvolution.getName()) &&
                Objects.equals(user.getName(), that.user.getName()) &&
                Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patternEvolution.getName(), user.getName(), rating);
    }
}
