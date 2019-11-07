package com.patternpedia.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class PatternSchema {

    @Id
    private UUID id;

    @OneToMany(mappedBy = "patternSchema", cascade = CascadeType.ALL)
    @OrderBy("position ASC")
    private List<PatternSectionSchema> patternSectionSchemas;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne
    @MapsId
    private PatternLanguage patternLanguage;
}
