package com.patternpedia.api.util.aggregator;

import com.patternpedia.api.entities.designmodel.DesignModelPatternEdge;
import com.patternpedia.api.entities.designmodel.DesignModelPatternEdgeId;
import com.patternpedia.api.entities.designmodel.DesignModelPatternInstance;
import lombok.extern.apachecommons.CommonsLog;

import java.util.*;


@CommonsLog
public abstract class ActiveMQAggregator extends Aggregator {


    @Override
    public abstract String aggregate(List<DesignModelPatternInstance> patternInstances, List<DesignModelPatternEdge> edges, Map<String, Object> query);


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
}
