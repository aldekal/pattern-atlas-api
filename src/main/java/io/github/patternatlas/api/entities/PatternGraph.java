package io.github.patternatlas.api.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.annotations.Type;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class PatternGraph extends EntityWithURI {

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Object graph;

    @Override
    public int hashCode() {
        return Objects.hash(graph);
    }
}
