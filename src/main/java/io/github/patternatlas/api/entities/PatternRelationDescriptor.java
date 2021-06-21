package io.github.patternatlas.api.entities;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.NoArgsConstructor;

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
