package com.patternpedia.api.entities.designmodel;

import com.patternpedia.api.entities.EntityWithURI;
import com.patternpedia.api.entities.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URI;
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

    @NaturalId(mutable = true)
    @Column(nullable = false)
    private String patternUri;

//    @OneToMany
//    private List<ConcreteSolutionOption> options;

    private Boolean isTemplate;

    private String template;

    private String reference;
}
