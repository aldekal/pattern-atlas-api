package com.patternpedia.api.entities.designmodel;

import com.patternpedia.api.entities.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
@Relation(value = "pattern", collectionRelation = "patterns")
public class DesignModelPatternInstance {

    @Id
    @GeneratedValue(generator = "pg-uuid")
    protected UUID patternInstanceId;

    @ManyToOne
    @EqualsAndHashCode.Include
    private DesignModel designModel;

    @ManyToOne
    @EqualsAndHashCode.Include
    private Pattern pattern;

    private DesignModelPatternGraphData graphData;

    @Transient
    private ConcreteSolution concreteSolution;


    public DesignModelPatternInstance(DesignModel designModel, Pattern pattern) {
        this.designModel = designModel;
        this.pattern = pattern;
    }
}
