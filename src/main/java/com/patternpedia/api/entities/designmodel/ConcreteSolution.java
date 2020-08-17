package com.patternpedia.api.entities.designmodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;


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

    @ElementCollection
    private List<String> properties;

    private String templateRef;

    private String aggregatorType;

    private Integer priority;
}
