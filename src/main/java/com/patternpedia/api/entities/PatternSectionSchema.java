package com.patternpedia.api.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
