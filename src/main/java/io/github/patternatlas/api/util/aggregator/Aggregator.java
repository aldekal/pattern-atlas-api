package io.github.patternatlas.api.util.aggregator;

import io.github.patternatlas.api.entities.designmodel.AggregationData;

public interface Aggregator {

    void aggregate(AggregationData aggregationData);
}
