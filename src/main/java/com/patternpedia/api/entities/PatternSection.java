package com.patternpedia.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class PatternSection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private PatternSectionSchema patternSectionSchema;

    @Lob
    private String value;
}
