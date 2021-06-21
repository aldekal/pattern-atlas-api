package io.github.patternatlas.api.entities.designmodel;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.springframework.hateoas.server.core.Relation;

import io.github.patternatlas.api.entities.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
