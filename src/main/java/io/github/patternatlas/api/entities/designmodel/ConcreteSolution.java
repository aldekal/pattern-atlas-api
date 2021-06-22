package io.github.patternatlas.api.entities.designmodel;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.hateoas.server.core.Relation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
@Relation(value = "concreteSolution", collectionRelation = "concreteSolutions")
public class ConcreteSolution {

    @Id
    @GeneratedValue(generator = "pg-uuid")
    protected UUID id;

    @Column(nullable = false)
    private String patternUri;

    private String name;

    private String templateUri;

    private String aggregatorType;
}
