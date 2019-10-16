package com.patternpedia.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = "patternSchema")
public class PatternSectionType implements Comparable<PatternSectionType> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String label;

    private String type;

    private Integer position;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private PatternSchema patternSchema;

    @Override
    public int compareTo(PatternSectionType o) {
        return position.compareTo(o.position);
    }
}
