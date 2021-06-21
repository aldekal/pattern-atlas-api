package io.github.ust.quantil.patternatlas.api.util.aggregator;

import io.github.ust.quantil.patternatlas.api.entities.designmodel.AggregationData;

public interface Aggregator {

    void aggregate(AggregationData aggregationData);
}
