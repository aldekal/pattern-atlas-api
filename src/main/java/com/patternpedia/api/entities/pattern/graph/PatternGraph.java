package com.patternpedia.api.entities.pattern.graph;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.patternpedia.api.entities.EntityWithURI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class PatternGraph extends EntityWithURI {

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Object graph;
}
