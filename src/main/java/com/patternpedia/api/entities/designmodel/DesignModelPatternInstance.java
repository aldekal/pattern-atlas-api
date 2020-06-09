package com.patternpedia.api.entities.designmodel;

import com.patternpedia.api.entities.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
@Relation(value = "pattern", collectionRelation = "patterns")
public class DesignModelPatternInstance {

    @EmbeddedId
    @EqualsAndHashCode.Exclude
    private DesignModelPatternId id;

    @ManyToOne
    @MapsId("designModelId")
    @EqualsAndHashCode.Include
    private DesignModel designModel;

    @ManyToOne
    @MapsId("patternId")
    @EqualsAndHashCode.Include
    private Pattern pattern;

    private DesignModelPatternGraphData graphData;


    public DesignModelPatternInstance(DesignModel designModel, Pattern pattern) {
        this.designModel = designModel;
        this.pattern = pattern;
        this.id = new DesignModelPatternId(designModel.getId(), pattern.getId(), UUID.randomUUID());
    }
}
