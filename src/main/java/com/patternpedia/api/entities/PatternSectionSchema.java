package com.patternpedia.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class PatternSectionSchema implements Comparable<PatternSectionSchema> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String label;

    private String name;

    private String type;

    private Integer position;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    private PatternSchema patternSchema;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany
    private List<PatternSection> patternSections;

    @Override
    public int compareTo(PatternSectionSchema o) {
        return position.compareTo(o.position);
    }
}
