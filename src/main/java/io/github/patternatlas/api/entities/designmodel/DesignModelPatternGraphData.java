package io.github.patternatlas.api.entities.designmodel;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class DesignModelPatternGraphData {

    private Double x;
    private Double y;
    private Double vx;
    private Double vy;
    private String type;
    private Integer index;
}
