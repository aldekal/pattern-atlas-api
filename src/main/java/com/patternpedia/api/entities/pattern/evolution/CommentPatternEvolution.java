package com.patternpedia.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.patternpedia.api.entities.rating.RatingPatternEvolution;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
public class UserEntity implements Serializable{

    @Id
    @GeneratedValue(generator = "pg-uuid")
    private UUID id;

    private String mail;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RatingPatternEvolution> ratingPatternEvolutions = new HashSet<RatingPatternEvolution>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        UserEntity that = (UserEntity) o;
        return id.equals(that.id) &&
                mail.equals(that.mail) &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mail, name);
    }

    @Override
    public String toString() {
        return "User: " + this.id.toString();
    }
}
