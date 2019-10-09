package com.patternpedia.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatternLanguage extends PatternGraph {

    @OneToMany
    @OrderBy("position ASC")
    private List<PatternSectionType> patternSchema;

//    @Type(type = "jsonb")
//    @Column(columnDefinition = "jsonb")
//    private JsonNode content;
}
