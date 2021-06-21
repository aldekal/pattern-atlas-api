package io.github.patternatlas.api.entities.designmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AggregationDataAndPatternEdge {

    private final AggregationData aggregationData;

    private final DesignModelPatternEdge edge;
}
