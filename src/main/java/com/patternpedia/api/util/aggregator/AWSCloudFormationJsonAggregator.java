package com.patternpedia.api.util.aggregator;

import com.patternpedia.api.entities.designmodel.DesignModelPatternEdge;
import com.patternpedia.api.entities.designmodel.DesignModelPatternInstance;

import java.util.List;
import java.util.Map;

public class AWSCloudFormationJsonAggregator extends ActiveMQAggregator {

    @Override
    public String aggregate(List<DesignModelPatternInstance> patternInstances, List<DesignModelPatternEdge> edges, Map<String, Object> query) {
        return null;
    }
}
