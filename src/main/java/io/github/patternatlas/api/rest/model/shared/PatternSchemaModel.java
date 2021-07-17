package io.github.patternatlas.api.rest.model.shared;

import io.github.patternatlas.api.entities.PatternSection;
import io.github.patternatlas.api.entities.PatternSectionSchema;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class PatternSchemaModel implements Comparable<PatternSchemaModel> {

    private String label;
    private String name;
    private String type;
    private Integer position;

    public PatternSchemaModel(PatternSectionSchema patternSectionSchema) {
        this.label = patternSectionSchema.getLabel();
        this.name = patternSectionSchema.getName();
        this.type = patternSectionSchema.getType();
        this.position = patternSectionSchema.getPosition();
    }

    public int compareTo(PatternSchemaModel o)
    {
        return  Integer.compare(this.position, o.position);
    }

}
