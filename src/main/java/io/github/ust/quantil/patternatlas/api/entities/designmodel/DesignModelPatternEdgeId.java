package io.github.ust.quantil.patternatlas.api.entities.designmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class DesignModelPatternEdgeId implements Serializable {

    protected UUID patternInstanceId1;

    protected UUID patternInstanceId2;
}
