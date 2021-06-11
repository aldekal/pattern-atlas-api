package com.patternatlas.api.entities;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
