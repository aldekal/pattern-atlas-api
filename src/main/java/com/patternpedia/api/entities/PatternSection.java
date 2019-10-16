package com.patternpedia.api.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class PatternSection {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private PatternSectionType type;

    @Lob
    private String value;

    @ManyToOne
    private Pattern pattern;
}
