package com.patternpedia.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.patternpedia.api.entities.rating.RatingDown;
import com.patternpedia.api.entities.rating.RatingPatternEvolution;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.catalina.User;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PatternEvolution extends EntityWithURI {

    private int rating;

    @JsonIgnore
    @OneToMany(mappedBy = "patternEvolution", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RatingPatternEvolution> userRating = new HashSet<RatingPatternEvolution>();

}
