package io.github.patternatlas.api.util.aggregator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import io.github.patternatlas.api.entities.designmodel.AggregationData;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
public abstract class ActiveMQAggregator extends AggregatorImpl {

    @Override
    public abstract void aggregate(AggregationData aggregationData);

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
