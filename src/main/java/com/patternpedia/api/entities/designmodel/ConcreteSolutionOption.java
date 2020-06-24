package com.patternpedia.api.entities.designmodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapsId;
import java.net.URI;
import java.util.List;
import java.util.UUID;


//@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class ConcreteSolutionOption {

    @Id
    private UUID concreteSolutionId;

    @Id
    private String key;

    private String value;
}
