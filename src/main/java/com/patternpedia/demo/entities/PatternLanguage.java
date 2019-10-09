package com.patternpedia.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatternLanguage extends PatternGraph {

    @OneToMany
    @OrderBy("position ASC")
    private List<PatternSectionType> patternSchema;

    public PatternLanguage(String uri, String name, SortedSet<Pattern> patterns, Set<PatternRelationDescriptor> patternRelationDescriptors, List<PatternSectionType> patternSchema) throws UnsupportedEncodingException {
        super(uri, name, patterns, patternRelationDescriptors);
        this.patternSchema = patternSchema;
    }

//    @Type(type = "jsonb")
//    @Column(columnDefinition = "jsonb")
//    private JsonNode content;
}
