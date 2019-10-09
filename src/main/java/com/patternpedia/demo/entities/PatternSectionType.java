package com.patternpedia.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatternSectionType implements Comparable<PatternSectionType> {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private String label;
    private String type;
    private Integer position;

    @Override
    public int compareTo(PatternSectionType o) {
        return position.compareTo(o.position);
    }
}
