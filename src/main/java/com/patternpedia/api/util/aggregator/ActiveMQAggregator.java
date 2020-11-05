package com.patternpedia.api.util.aggregator;

import com.patternpedia.api.entities.designmodel.AggregationData;
import com.patternpedia.api.entities.designmodel.DesignModelPatternEdge;
import com.patternpedia.api.entities.designmodel.DesignModelPatternEdgeId;
import lombok.extern.apachecommons.CommonsLog;

import java.util.*;
import java.util.function.BiFunction;


@CommonsLog
public abstract class ActiveMQAggregator extends AggregatorImpl {

    @Override
    public abstract void aggregate(AggregationData aggregationData);


    public static Object getQueueList(Collection<DesignModelPatternEdge> edges) {
        if (edges.size() == 1) {
            DesignModelPatternEdgeId edgeId = edges.iterator().next().getEdgeId();
            return "queue" + edgeId.getPatternInstanceId2().toString();
        }
        if (edges.size() >= 2) {
            Set<String> queueNames = new HashSet<>();
            for (DesignModelPatternEdge edge : edges) {
                queueNames.add("queue" + edge.getEdgeId().getPatternInstanceId2().toString());
            }
            return queueNames;
        }
        return null;
    }


    protected static void addInputOutputChannelContext(AggregationData aggregationData) {

        final String channelName = getIdentifier(aggregationData.getSource());

        BiFunction computeMapEntries = (Object key, Object value) -> {
            String newValue = channelName;

            if (value == null || value.equals(newValue)) {
                return newValue;
            } else if (value instanceof Collection) {
                List values = new ArrayList((Collection) value);
                values.add(newValue);
                return values;
            } else {
                return Arrays.asList(value, newValue);
            }
        };

        Map<String, Object> context = aggregationData.getTemplateContext();
        context.compute(aggregationData.getSource().getPatternInstanceId() + "-input", computeMapEntries);
        if (aggregationData.getTarget() != null) {
            context.compute(aggregationData.getTarget().getPatternInstanceId().toString() + "-output", computeMapEntries);
        }
    }
}
