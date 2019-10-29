package com.patternpedia.api.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class PatternRelationDescriptor {

    private static ObjectMapper mapper = new ObjectMapper();

    @Id
    @GeneratedValue(generator = "pg-uuid")
    private UUID id;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Object description;

    private String type;

    public void setDescription(Object description) {
        this.description = mapper.valueToTree(description);
    }

}
