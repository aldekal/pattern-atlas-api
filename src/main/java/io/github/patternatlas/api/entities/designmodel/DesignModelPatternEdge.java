package io.github.patternatlas.api.entities.designmodel;

import static java.lang.Boolean.TRUE;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class DesignModelPatternEdge {

    @EmbeddedId
    @EqualsAndHashCode.Exclude
    private DesignModelPatternEdgeId edgeId;

    @ManyToOne
    @EqualsAndHashCode.Include
    private DesignModel designModel;

    @ManyToOne
    @MapsId("patternInstanceId1")
    @EqualsAndHashCode.Include
    private DesignModelPatternInstance patternInstance1;

    @ManyToOne
    @MapsId("patternInstanceId2")
    @EqualsAndHashCode.Include
    private DesignModelPatternInstance patternInstance2;

    private Boolean isDirectedEdge;

    private String type;

    private String description;

    public boolean isDirectedEdge() {
        return TRUE.equals(isDirectedEdge);
    }

    public void setPatternInstance1(DesignModelPatternInstance patternInstance) {
        if (edgeId == null) {
            this.edgeId = new DesignModelPatternEdgeId();
        }
        this.edgeId.setPatternInstanceId1(patternInstance.getPatternInstanceId());
        this.patternInstance1 = patternInstance;
    }

    public void setPatternInstance2(DesignModelPatternInstance patternInstance) {
        if (edgeId == null) {
            this.edgeId = new DesignModelPatternEdgeId();
        }
        this.edgeId.setPatternInstanceId2(patternInstance.getPatternInstanceId());
        this.patternInstance2 = patternInstance;
    }
}
