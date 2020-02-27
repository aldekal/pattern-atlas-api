package com.patternpedia.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class User {

    @Id
    @GeneratedValue(generator = "pg-uuid")
    private UUID id;

    private String mail;

    private String name;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private PatternEvolution patternEvolution;


}
