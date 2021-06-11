package com.patternatlas.api.entities.designmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class AggregationDataAndPatternEdge {

    private AggregationData aggregationData;

    private DesignModelPatternEdge edge;
}
