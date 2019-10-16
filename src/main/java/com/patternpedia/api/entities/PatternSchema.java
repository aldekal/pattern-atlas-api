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
@ToString(exclude = "patterns")
public class PatternSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("position ASC")
    private List<PatternSectionType> patternSectionTypes;

    @JsonIgnore
    @OneToMany
    private List<Pattern> patterns;

}
